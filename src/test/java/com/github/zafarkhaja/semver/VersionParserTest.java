/*
 * The MIT License
 *
 * Copyright 2012-2023 Zafar Khaja <zafarkhaja@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.zafarkhaja.semver;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Zafar Khaja {@literal <zafarkhaja@gmail.com>}
 */
class VersionParserTest {

    @Test
    void shouldParseNormalVersion() {
        NormalVersion version = VersionParser.parseVersionCore("1.0.0");
        assertEquals(new NormalVersion(1, 0, 0), version);
    }

    @Test
    void shouldRaiseErrorIfNumericIdentifierHasLeadingZeroes() {
        assertThrows(
            ParseException.class,
            () -> VersionParser.parseVersionCore("01.1.0"),
            "Numeric identifier MUST NOT contain leading zeroes"
        );
    }

    @Test
    void shouldParsePreReleaseVersion() {
        MetadataVersion preRelease = VersionParser.parsePreRelease("beta-1.1");
        assertEquals(new MetadataVersion(new String[] {"beta-1", "1"}), preRelease);
    }

    @Test
    void shouldNotAllowDigitsInPreReleaseVersion() {
        assertThrows(
            ParseException.class,
            () -> VersionParser.parsePreRelease("alpha.01"),
            "Should not allow digits in pre-release version"
        );
    }

    @Test
    void shouldRaiseErrorForEmptyPreReleaseIdentifier() {
        assertThrows(
            ParseException.class,
            () -> VersionParser.parsePreRelease("beta-1..1"),
            "Identifiers MUST NOT be empty"
        );
    }

    @Test
    void shouldParseBuildMetadata() {
        MetadataVersion build = VersionParser.parseBuild("build.1");
        assertEquals(new MetadataVersion(new String[] {"build", "1"}), build);
    }

    @Test
    void shouldAllowDigitsInBuildMetadata() {
        assertDoesNotThrow(
            () -> VersionParser.parseBuild("build.01"),
            "Should allow digits in build metadata"
        );
    }

    @Test
    void shouldRaiseErrorForEmptyBuildIdentifier() {
        assertThrows(
            ParseException.class,
            () -> VersionParser.parseBuild(".build.01"),
            "Identifiers MUST NOT be empty"
        );
    }

    @Test
    void shouldParseValidSemVer() {
        VersionParser parser = new VersionParser("1.0.0-rc.2+build.05");
        Version version = parser.parse(null);
        assertEquals(
            new Version(
                new NormalVersion(1, 0, 0),
                new MetadataVersion(new String[] {"rc", "2"}),
                new MetadataVersion(new String[] {"build", "05"})
            ),
            version
        );
    }

    @Test
    void shouldRaiseErrorForIllegalInputString() {
        for (String illegal : new String[] { "", null }) {
            assertThrows(
                IllegalArgumentException.class,
                () -> new VersionParser(illegal),
                "Should raise error for illegal input string"
            );
        }
    }
}
