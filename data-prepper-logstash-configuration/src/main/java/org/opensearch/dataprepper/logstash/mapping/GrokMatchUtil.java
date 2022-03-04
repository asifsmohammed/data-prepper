/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.dataprepper.logstash.mapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrokMatchUtil {
    private static final String GROK_MATCH_PATTERN_REGEX = "(%\\{)([^{}:]+)(:)?([^{}:]+)?(:)?([a-zA-Z]+)?(}(\\s+)?)";
    private static final Pattern GROK_MATCH_PATTERN = Pattern.compile(GROK_MATCH_PATTERN_REGEX);

    private GrokMatchUtil() {
    }

    public static String convertGrokMatchPattern(final String matchPattern) {
        StringBuilder convertedGrokMatchPattern = new StringBuilder();
        Matcher grokMatchPatternMatcher = GROK_MATCH_PATTERN.matcher(matchPattern);
        while(grokMatchPatternMatcher.find()) {
            convertedGrokMatchPattern.append(getConvertedMatchPattern(grokMatchPatternMatcher.group()));
        }
        if (convertedGrokMatchPattern.toString().isEmpty())
            return matchPattern;

        return convertedGrokMatchPattern.toString();
    }

    private static String getConvertedMatchPattern(String matchPatternGroup) {
        final String SEPARATOR = ":";
        StringBuilder convertedGrokMatchPatternGroup = new StringBuilder();
        int firstSeparatorIndex = matchPatternGroup.indexOf(SEPARATOR);
        int secondSeparatorIndex = matchPatternGroup.lastIndexOf(SEPARATOR);

        if (firstSeparatorIndex != secondSeparatorIndex) {
            String nestedField = matchPatternGroup.substring(firstSeparatorIndex + 1, secondSeparatorIndex);
            String convertedMatchPatternGroup = NestedSyntaxConverter.convertNestedSyntaxToJsonPointer(nestedField);
            convertedGrokMatchPatternGroup.append(matchPatternGroup, 0, firstSeparatorIndex + 1);
            convertedGrokMatchPatternGroup.append(convertedMatchPatternGroup);
            convertedGrokMatchPatternGroup.append(matchPatternGroup.substring(secondSeparatorIndex));
        }
        else if (firstSeparatorIndex != -1) {
            String nestedField = matchPatternGroup.substring(firstSeparatorIndex + 1, matchPatternGroup.indexOf("}"));
            String convertedMatchPatternGroup = NestedSyntaxConverter.convertNestedSyntaxToJsonPointer(nestedField);
            convertedGrokMatchPatternGroup.append(matchPatternGroup, 0, firstSeparatorIndex + 1);
            convertedGrokMatchPatternGroup.append(convertedMatchPatternGroup);
            convertedGrokMatchPatternGroup.append(matchPatternGroup.substring(matchPatternGroup.lastIndexOf("}")));
        }
        if (convertedGrokMatchPatternGroup.toString().isEmpty())
            return matchPatternGroup;
        return convertedGrokMatchPatternGroup.toString();
    }
}
