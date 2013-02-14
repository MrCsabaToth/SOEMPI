/**
 * LingPipe v. 3.9
 * Copyright (C) 2003-2010 Alias-i
 *
 * This program is licensed under the Alias-i Royalty Free License
 * Version 1 WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Alias-i
 * Royalty Free License Version 1 for more details.
 *
 * You should have received a copy of the Alias-i Royalty Free License
 * Version 1 along with this program; if not, visit
 * http://alias-i.com/lingpipe/licenses/lingpipe-license-1.txt or contact
 * Alias-i, Inc. at 181 North 11th Street, Suite 401, Brooklyn, NY 11211,
 * +1 (718) 290-9170.
 */

package org.openhie.openempi.stringcomparison.metrics;

import java.util.Arrays;

/**
 * The <code>JaroWinklerDistance</code> class implements the original
 * Jaro string comparison as well as Winkler's modifications.  As a
 * distance measure, Jaro-Winkler returns values between
 * <code>0</code> (exact string match) and <code>1</code> (no matching
 * characters).  Note that this is reversed from the original
 * definitions of Jaro and Winkler in order to produce a distance-like
 * ordering.  The original Jaro-Winkler string comparator returned
 * <code>1</code> for a perfect match and <code>0</code> for
 * complete mismatch; our method returns one minus the Jaro-Winkler
 * measure.
 *
 * <p>The Jaro-Winkler distance measure was developed for name comparison
 * in the U.S. Census.  It is designed to compae surnames to surnames
 * and given names to given names, not whole names to whole names.
 * There is no character-specific information in this implementation,
 * but assumptions are made about typical lengths and the significance
 * of initial matches that may not apply to all languages.
 *
 * <p>The easiest way to understand the Jaro measure and the Winkler
 * variants is procedurally.  The Jaro measure involves two steps,
 * first to compute the number of &quot;matches&quot; and second to
 * compute the number of &quot;transpositions&quot;.  The Winkler
 * adjustment involves a final rescoring based on an exact match score
 * for the initial characters of both strings.
 *
 * <h4>Formal Definition of Jaro-Winkler Distance</h4>
 *
 * <p>Suppose we are comparing character sequences <code>cs1</code>
 * and <code>cs2</code>.  The Jaro-Winkler distance is defined by
 * the following steps.  After the definitions, we consider some
 * examples.
 *
 * <p><b>Step 1: Matches:</b> The match phase is a greedy alignment
 * step of characters in one string against the characters in another
 * string.  The maximum distance (measured by array index) at which
 * characters may be matched is defined by:
 *
 * <pre>
 *   matchRange = max(cs1.length(), cs2.length()) / 2 - 1</pre>
 *
 * <p>The match phase is a greedy alignment that proceeds character by
 * character through the first string, though the distance metric is
 * symmetric (that, is reversing the order of arguments does not
 * affect the result).  For each character encountered in the first
 * string, it is matched to the first unaligned character in the
 * second string that is an exact character match.  If there is no
 * such character within the match range window, the character is left
 * unaligned.
 *
 * <p><b>Step 2: Transpositions:</b> After matching, the subsequence
 * of characters actually matched in both strings is extracted.  These
 * subsequences will be the same length.  The number of characters in
 * one string that do not line up (by index in the matched
 * subsequence) with identical characters in the other string is the
 * number of &quot;half transpositions&quot;.  The total number of
 * transpoisitons is the number of half transpositions divided by two,
 * rounding down.
 *
 * <p>The Jaro distance is then defined in terms of the number
 * of matching characters <code>matches</code> and the number
 * of transpositions, <code>transposes</code>:
 *
 * <pre>
 *   jaroProximity(cs1,cs2)
 *     = ( matches(cs1,cs2) / cs1.length()
 *         + matches(cs1,cs2) / cs2.length()
 *         + (matches(cs1,cs2) - transposes(cs1,cs2)) / matches(cs1,cs2) ) / 3
 *
 *   jaroDistance(cs1,cs2) = 1 - jaroProximity(cs1,cs2)</pre>
 *
 * <p>In words, the measure is the average of three values; (a) the
 * percentage of the first string matched, (b) the percentage of the
 * second string matched, and (c) the percentage of matches that were
 * not transposed.
 *
 * <p><b>Step 3: Winkler Modification</b> The Winkler modification to
 * the Jaro comparison, resulting in the Jaro-Winkler comparison,
 * boosts scores for strings that match character for character
 * initially.  Let <code>boostThreshold</code> be the minimum score
 * for a string that gets boosted.  This value was set to
 * <code>0.7</code> in Winkler's papers (see references below).  If
 * the Jaro score is below the boost threshold, the Jaro score is
 * returned unadjusted.  The second parameter for the Winkler
 * modification is the size of the initial prefix considered,
 * <code>prefixSize</code>.  The prefix size was set to <code>4</code>
 * in Winkler's papers.  Next, let
 * <code>prefixMatch(cs1,cs2,prefixSize)</code> be the number of
 * characters in the prefix of <code>cs1</code> and <code>cs2</code>
 * that exactly match (by original index), up to a maximum of
 * <code>prefixSize</code>.  The modified distance is then defined to
 * be:
 *
 * <pre>
 *   jaroWinklerProximity(cs1,cs2,boostThreshold,prefixSize)
 *     = jaroMeasure(cs1,cs2) <= boostThreshold
 *     ? jaroMeasure(cs1,cs2)
 *     : jaroMeasure(cs1,cs2)
 *       + 0.1 * prefixMatch(cs1,cs2,prefixSize) * (1.0 - jaroDistance(cs1,cs2))
 *
 *   jaroWinklerDistance(cs1,cs2,boostThreshold,prefixSize)
 *     = 1 - jaroWinklerProximity(cs1,cs2,boostThreshold,prefixSize)</pre>
 *
 * <p><b>Examples:</b> We will present the alignment steps in the form
 * of tables, with offsets in the second string below the first string
 * positions that match.  For a simple example, consider comparing the
 * given (nick)name <code>AL</code> to itself.  Both strings are of
 * length 2.  Thus the maximum match distance is <code>max(2,2)/2 - 1
 * = 0</code>, meaning all matches must be exact.  The matches are
 * illustrated in the following table:
 *
 * <table cellpadding="3" border="1" style="margin-left: 2em">
 * <tr><td><code>cs1</code></td><td>A</td><td>L</td></tr>
 * <tr><td>matches</td><td>0</td><td>1</td></tr>
 * <tr><td><code>cs2</code></td><td>A</td><td>L</td></tr>
 * </table>
 *
 * <p>The notation in the matches row is meant to indicate that the
 * <code>A</code> at index <code>0</code> in <code>cs1</code> is
 * matched to the <code>A</code> at index <code>0</code> in
 * <code>cs2</code>.  Similarlty for the <code>L</code> at index 1 in
 * <code>cs1</code>, which matches the <code>L</code> at index 1 in
 * <code>cs2</code>.  This results in <code>matches(AL,AL) = 2</code>.
 * There are no transpositions, so the Jaro distance is just:
 *
 * <pre>
 *   jaroProximity(AL,AL) = 1/3*(2/2 + 2/2 + (2-0)/2) = 1.0</pre>
 *
 * <p>Applying the Winkler modification yields the same result:
 *
 * <pre>
 *   jaroWinklerProximity(AL,AL) = 1 +  0.1 * 2 * (1.0 - 1) = 1.0</pre>
 *
 * <p>Next consider a more complex case, matching <code>MARTHA</code> and
 * <code>MARHTA</code>.  Here the match distance is <code>max(5,5)/2 -
 * 1 = 1</code>, allowing matching characters to be up to one
 * character away.  This yields the following alignment.
 *
 * <table cellpadding="3" border="1" style="margin-left: 2em">
 * <tr><td><code>cs1</code></td>
 *     <td>M</td><td>A</td><td>R</td><td><b>T</b></td><td><b>H</b></td><td>A</td>
 * </tr>
 * <tr><td>matches</td>
 *     <td>0</td><td>1</td><td>2</td><td>4</td><td>3</td><td>5</td>
 * </tr>
 * <tr><td><code>cs2</code></td>
 *     <td>M</td><td>A</td><td>R</td><td><b>H</b></td><td><b>T</b></td><td>A</td>
 * </tr>
 * </table>
 *
 * <p>Note that the <code>T</code> at index 3 in the first string
 * aligns with the <code>T</code> at index 4 in the second string,
 * whereas the <code>H</code> at index 4 in the first string alings
 * with the <code>H</code> at index 3 in the second string.  The
 * strings that do not directly align are rendered in bold.  This is
 * an instance of a transposition.  The number of half transpositions
 * is determined by comparing the subsequences of the first and second
 * string matched, namely <code>MARTHA</code> and <code>MARHTA</code>.
 * There are two positions with mismatched characters, 3 and 4.  This
 * results in two half transpositions, or a single transposition, for
 * a Jaro distance of:
 *
 * <pre>
 *   jaroProximity(MARTHA,MARHTA) = 1/3 * (6/6 + 6/6 + (6 - 1)/6) = 0.944</pre>
 *
 * Three initial characters match, <code>MAR</code>, for a Jaro-Winkler
 * distance of:
 *
 * <pre>
 *   jaroWinklerProximity(MARTHA,MARHTA) = 0.944 + 0.1 * 3 * (1.0 - 0.944) = 0.961</pre>
 *
 * <p>Next, consider matching strings of different lengths, such as
 * <code>JONES</code> and <code>JOHNSON</code>:
 *
 * <table cellpadding="3" border="1" style="margin-left: 2em">
 * <tr><td><code>cs1</code></td>
 *     <td>J</td><td>O</td><td>N</td><td><i>E</i></td><td>S</td><td></td><td></td>
 * </tr>
 * <tr><td>matches</td>
 *     <td>0</td><td>1</td><td>3</td><td>-</td><td>5</td><td></td><td></td>
 * </tr>
 * <tr><td><code>cs2</code></td>
 *     <td>J</td><td>O</td><td><i>H</i></td><td>N</td><td>S</td><td><i>O</i></td><td><i>N</i></td>
 * </tr>
 * </table>
 *
 * <p>The unmatched characters are rendered in italics.  Here the
 * subsequence of matched characters for the two strings are <code>JONS</code> and
 * <code>JONS</code>, so there are no transpositions.  Thus the Jaro
 * distance is:
 *
 * <pre>
 *   jaroProximity(JONES,JOHNSON)
 *     = 1/3 * (4/5 + 4/7 + (4 - 0)/4) = 0.790</pre>
 *
 * <p>The strings <code>JONES</code> and <code>JOHNSON</code> only
 * match on their first two characters, <code>JO</code>, so the
 * Jaro-Winkler distance is:
 *
 * <pre>
 *   jaroWinklerProximity(JONES,JOHNSON)
 *     = .790 + 0.1 * 2 * (1.0 - .790) = 0.832</pre>
 *
 * <p>We will now consider some artificial examples not drawn from
 * (Winkler 2006).  First, compare <code>ABCVWXYZ</code> and
 * <code>CABVWXYZ</code>, which are of length 8, allowing alignments
 * up to <code>8/4 - 1 = 3</code> positions away.  This leads
 * to the following alignment:
 *
 * <table cellpadding="3" border="1" style="margin-left: 2em">
 * <tr><td><code>cs1</code></td>
 *     <td><b>A</b></td><td><b>B</b></td><td><b>C</b></td><td>V</td><td>W</td><td>X</td><td>Y</td><td>Z</td>
 * </tr>
 * <tr><td>matches</td>
 *     <td>1</td><td>2</td><td>0</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td>
 * </tr>
 * <tr><td><code>cs2</code></td>
 *     <td><b>C</b></td><td><b>A</b></td><td><b>B</b></td><td>V</td><td>W</td><td>X</td><td>Y</td><td>Z</td>
 * </tr>
 * </table>
 *
 * <p>Here, there are 8/8 matches in both strings.  There are only
 * three half-transpositions, in the first three characters,
 * because no position of <code>CAB</code> has an identical character
 * to <code>ABC</code>.  This yields a total of one transposition,
 * for a Jaro score of:
 *
 * <pre>
 *   jaroProximity(ABCVWXYZ,CABVWXYZ)
 *     = 1/3 * (8/8 + 8/8 + (8-1)/8) = .958</pre>
 *
 * <p>There is no initial prefix match, so the Jaro-Winkler comparison
 * produces the same result.  Now consider matching
 * <code>ABCVWXYZ</code> to <code>CBAWXYZ</code>.  Here, the initial
 * alignment is <code>2, 1, 0</code>, which yields only two half
 * transpositions.  Thus under the Jaro distance, <code>ABC</code> is
 * closer to <code>CBA</code> than to <code>CAB</code>, though due to
 * integer rounding in computing the number of transpositions, this
 * will only affect the final result if there is a further
 * transposition in the strings.
 *
 * <p>Now consider the 10-character string <code>ABCDUVWXYZ</code>.
 * This allows matches up to <code>10/2 - 1 = 4</code> positions away.
 * If matched against <code>DABCUVWXYZ</code>, the result is 10
 * matches, and 4 half transposes, or 2 transposes.  Now consider
 * matching <code>ABCDUVWXYZ</code> against <code>DBCAUVWXYZ</code>.
 * Here, index 0 in the first string (<code>A</code>) maps to
 * index 3 in the second string, and index 3 in the first string
 * (<code>D</code>) maps to index 0 in the second string, but
 * positions 1 and 2 (<code>B</code> and <code>C</code>) map to
 * themselves.  Thus when comparing the output, there are only two
 * half transpositions, thus making the second example
 * <code>DBCAUVWXYZ</code> closer than <code>DABCUVWXYZ</code> to the
 * first string <code>ABCDUVWXYZ</code>.
 *
 * <p>Note that the transposition count cannot be determined solely by
 * the mapping.  For instance, the string <code>ABBBUVWXYZ</code>
 * matches <code>BBBAUVWXYZ</code> with alignment <code>4, 0, 1, 2, 5, 6, 7,
 * 8, 9, 1</code>.  But there are only two half-transpositions, because
 * only index 0 and index 3 mismatch in the subsequences of matching
 * characters. Contrast this with <code>ABCDUVWXYZ</code> matching
 * <code>DABCUVWXYZ</code>, which has the same alignment, but four
 * half transpositions.
 *
 * <p>The greedy nature of the alignment phase in the Jaro-Winkler
 * algorithm actually prevents the optimal alignments from being found
 * in some cases.  Consider the alignment of <code>ABCAWXYZ</code>
 * with <code>BCAWXYZ</code>:
 *
 * <table cellpadding="3" border="1" style="margin-left: 2em">
 * <tr><td><code>cs1</code></td>
 *     <td><b>A<b></td><td><b>B</b></td><td><b>C</b></td><td><i>A</i></td><td>W</td><td>X</td><td>Y</td><td>Z</td>
 * </tr>
 * <tr><td>matches</td>
 *     <td>2</td><td>0</td><td>1</td><td>-</td><td>3</td><td>4</td><td>5</td><td>6</td>
 * </tr>
 * <tr><td><code>cs2</code></td>
 *     <td><b>B</b></td><td><b>C</b></td><td><b>A</b></td><td>W</td><td>X</td><td>Y</td><td>Z</td><td>&nbsp;</td>
 * </tr>
 * </table>
 *
 * <p>Here the first pair of <code>A</code> characters are matched,
 * leading to three half transposes (the first three matched
 * characters).  A better scoring, though illegal, alignment would be
 * the following, because it has the same number of matches, but no
 * transposes:
 *
 * <p><table cellpadding="3" border="1" style="margin-left: 2em">
 * <tr><td><code>cs1</code></td>
 *     <td><i>A</i></td><td><b>B</b></td><td><b>C</b></td><td><b>A</b></td><td>W</td><td>X</td><td>Y</td><td>Z</td>
 * </tr>
 * <tr><td>matches</td>
 *     <td style="background-color:#FF9">-</td><td>0</td><td>1</td><td style="background-color:#FF9">2</td><td>3</td><td>4</td><td>5</td><td>6</td>
 * </tr>
 * <tr><td><code>cs2</code></td>
 *     <td><b>B</b></td><td><b>C</b></td><td><b>A</b></td><td>W</td><td>X</td><td>Y</td><td>Z</td><td>&nbsp;</td>
 * </tr>
 * </table>
 *
 * <p>The illegal links are highlighted in yellow.  Note that neither alignment
 * matches in the initial character, so the Winkler adjustments do not apply.
 *
 * <h4>Implementation Notes</h4>
 *
 * <p>This class's implementation is a literal translation of the C
 * algorithm used in William E. Winkler's papers and for the 1995
 * U.S. Census Deduplication.  The algorithm is the work of
 * multiple authors and available from the folloiwng link:
 *
 * <ul>
 * <li>
 * Winkler, Bill, George McLaughlin, Matt Jaro and Marueen Lynch.  1994.
 * <a href="http://www.census.gov/geo/msb/stand/strcmp.c">strcmp95.c</a>,
 * Version 2. United States Census Bureau.
 * </li>
 * </ul>
 *
 * <p> Unlike the C version, the {@link
 * #distance(CharSequence,CharSequence)} and {@link
 * #proximity(CharSequence,CharSequence)} methods do not require its
 * inputs to be padded with spaces.  In addition, spaces are treated
 * just like any other characters within the algorithm itself.  There
 * is also no case normalization in this class's version.
 * Furthermore, the boundary conditions are changed so that two empty
 * strings return a score of <code>1.0</code> rather than zero, as in
 * the original algorithm.
 *
 * <p>Jaro's origial implementation is described in:
 *
 * <ul>
 * <li>Jaro, Matthew A. 1989. Advances in Record-Linkage Methodology as
Applied to Matching the 1985 Census of Tampa, Florida. <i>Journal of the
American Statistical Association</i> <b>84</b>(406):414--420.
 * </ul>
 *
 * <p>Winkler's modified algorithm, along with applications in record
 * linkage, are described in the following highly readable survey
 * article:
 *
 * <ul>
 * <li>
 * Winkler, William E.  2006.
 * <a href="http://www.census.gov/srd/papers/pdf/rrs2006-02.pdf">Overview of
 * Record Linkage and Current Research Directions</a>.
 * Statistical Research Division, U.S. Census Bureau.
 * </li>
 * </ul>
 *
 * This document provides test cases in Table 6, which are the basis
 * for the unit tests for this class (though note the three 0.0
 * results in the table do not agree with the return results of
 * <code>strcmp95.c</code> or the results of this class, which matches
 * <code>strcmp95.c</code>).  The description of the matching
 * procedure above is based on the actual <code>strcmp95</code> code,
 * the boundary conditions of which are not obvious from the text
 * descriptions in the literature.  An additional difference is that
 * <code>strcmp95</code>, but not the algorithms in Winkler's papers
 * nor the algorithm in this class, provides the possibility of
 * partial matches with similar-sounding characters
 * (e.g. <code>c</code> and <code>k</code>).
 *
 * <h4>Acknowledgements</h4>
 *
 * <p>We'd like to thank Bill Winkler for helping us understand the
 * versions of the algorithm and providing the <code>strcmp95.c</code>
 * code as a reference implementation.
 *
 * @author  Bob Carpenter
 * @version 3.0
 * @since   LingPipe2.4
 */
public class JaroWinklerAliasiDistanceMetric extends AbstractDistanceMetric
{
    private final double mWeightThreshold;
    private final int mNumChars;

    /**
     * A Jaro-Winkler distance with defaults set as
     * in Winkler's papers.
     */
	public JaroWinklerAliasiDistanceMetric() {
        mWeightThreshold = 0.7;
        mNumChars = 4;
	}

    /**
     * Construct a Winkler-modified Jaro string distance with the
     * specified weight threshold for refinement and an initial number
     * of characters over which to reweight.  See the class
     * documentation above for more information on the exact algorithm
     * and its parameters.
     */
    public JaroWinklerAliasiDistanceMetric(double weightThreshold, int numChars) {
        mNumChars = numChars;
        mWeightThreshold = weightThreshold;
    }

	public double score(Object value1, Object value2) {
		if (missingValues(value1, value2)) {
			return handleMissingValues(value1, value2);
		}
		String string1 = value1.toString();
		String string2 = value2.toString();		
		double distance = proximity(upperCase(string1), upperCase(string2));
		log.trace("Computed the distance between :" + string1 + ": and :" + string2 + ": to be " + distance);
		return distance;
	}

    /**
     * Return the Jaro-Winkler comparison value between the specified
     * character sequences.  The comparison is symmetric and will fall
     * in the range <code>0</code> (no match) to <code>1</code>
     * (perfect match)inclusive.  See the class definition above for
     * an exact definition of Jaro-Winkler string comparison.
     *
     * <p>The method {@link #distance(String,String)} returns
     * a distance measure that is one minus the comparison value.
     *
     * @param cSeq1 First character sequence to compare.
     * @param cSeq2 Second character sequence to compare.
     * @return The Jaro-Winkler comparison value for the two character
     * sequences.
     */
    public double proximity(String cSeq1, String cSeq2) {
        int len1 = cSeq1.length();
        int len2 = cSeq2.length();
        if (len1 == 0)
            return len2 == 0 ? 1.0 : 0.0;

        int  searchRange = Math.max(0,Math.max(len1,len2)/2 - 1);

        boolean[] matched1 = new boolean[len1];
        Arrays.fill(matched1,false);
        boolean[] matched2 = new boolean[len2];
        Arrays.fill(matched2,false);

        int numCommon = 0;
        for (int i = 0; i < len1; ++i) {
            int start = Math.max(0,i-searchRange);
            int end = Math.min(i+searchRange+1,len2);
            for (int j = start; j < end; ++j) {
                if (matched2[j]) continue;
                if (cSeq1.charAt(i) != cSeq2.charAt(j))
                    continue;
                matched1[i] = true;
                matched2[j] = true;
                ++numCommon;
                break;
            }
        }
        if (numCommon == 0) return 0.0;

        int numHalfTransposed = 0;
        int j = 0;
        for (int i = 0; i < len1; ++i) {
            if (!matched1[i]) continue;
            while (!matched2[j]) ++j;
            if (cSeq1.charAt(i) != cSeq2.charAt(j))
                ++numHalfTransposed;
            ++j;
        }
        // System.out.println("numHalfTransposed=" + numHalfTransposed);
        int numTransposed = numHalfTransposed/2;

        // System.out.println("numCommon=" + numCommon
        // + " numTransposed=" + numTransposed);
        double numCommonD = numCommon;
        double weight = (numCommonD/len1
                         + numCommonD/len2
                         + (numCommon - numTransposed)/numCommonD)/3.0;

        if (weight <= mWeightThreshold) return weight;
        int max = Math.min(mNumChars,Math.min(cSeq1.length(),cSeq2.length()));
        int pos = 0;
        while (pos < max && cSeq1.charAt(pos) == cSeq2.charAt(pos))
            ++pos;
        if (pos == 0) return weight;
        return weight + 0.1 * pos * (1.0 - weight);

    }

}
