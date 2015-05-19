/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.googlejavaformat;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.NavigableMap;

/**
 * An input to the formatter.
 */
public abstract class Input extends InputOutput {
  /**
   * A {@code Tok} ("tock") is a token, or a comment, or a newline, or a maximal string of blanks. A
   * token {@code Tok} underlies a {@link Token}, and each other {@code Tok} is attached to a single
   * {@code Token}. Tokens and comments have indices; white space {@code Tok}s do not.
   */
  public interface Tok {
    /**
     * Return the {@code Tok}'s index.
     * @return its index
     */
    int getIndex();

    /**
     * Return the {@code Tok}'s {@code 0}-based position.
     * @return its position
     */
    int getPosition();

    /**
     * Return the {@code Tok}'s {@code 0}-based column number.
     * @return its column number
     */
    int getColumn();

    /**
     * Return the {@code Tok}'s text.
     * @return its text
     */
    String getText();

    /**
     * Return the {@code Tok}'s original text (before processing Unicode escapes).
     * @return its original text
     */
    String getOriginalText();

    /**
     * Is the {@code Tok} a newline?
     * @return whether it is a newline
     */
    boolean isNewline();

    /**
     * Is the {@code Tok} a "//" comment?
     * @return whether it is a "//" comment
     */
    boolean isSlashSlashComment();

    /**
     * Is the {@code Tok} a "/*" comment?
     * @return whether it is a "/*" comment
     */
    boolean isSlashStarComment();

    /**
     * Is the {@code Tok} a comment?
     * @return whether it is a comment
     */
    boolean isComment();
  }

  /**
   * A {@code Token} is a language-level token.
   */
  public interface Token {
    /**
     * Get the token's {@link Tok}.
     * @return the token's {@link Tok}
     */
    Tok getTok();

    /**
     * Get the earlier {@link Tok}s assigned to this {@code Token}.
     * @return the earlier {@link Tok}s assigned to this {@code Token}
     */
    ImmutableList<? extends Tok> getToksBefore();

    /**
     * Get the later {@link Tok}s assigned to this {@code Token}.
     * @return the later {@link Tok}s assigned to this {@code Token}
     */
    ImmutableList<? extends Tok> getToksAfter();
  }

  /**
   * Get the input tokens.
   * @return the input tokens
   */
  public abstract ImmutableList<? extends Token> getTokens();

  /**
   * Get the navigable map from position to {@link Token}. Used to look for tokens following a given
   * one.
   * @return the navigable map from position to {@link Token}
   */
  public abstract NavigableMap<Integer, ? extends Token> getPositionTokenMap();

  public abstract ImmutableMap<Integer, Integer> getPositionToColumnMap();

  public abstract String getText();
  
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("super", super.toString()).toString();
  }

  /** The input filename. */
  public abstract String filename();

  /** Converts a character offset in the input to a line number. */
  public abstract int getLineNumber(int inputPosition);

  /**
   * Construct a diagnostic. Populates the input filename, and converts
   * character offsets to numbers.
   * */
  public FormatterDiagnostic createDiagnostic(int inputPosition, String message) {
    return new FormatterDiagnostic(filename(), getLineNumber(inputPosition), message);
  }
}
