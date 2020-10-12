package org.ethelred.util;

import static org.junit.Assert.assertEquals;

import org.testng.annotations.Test;

/** @author Neil Traft */
public class Util_ConvertGlobToRegex_Test {

  @Test
  public void star_becomes_dot_star() throws Exception {
    assertEquals("gl.*b", Util.convertGlobToRegex("gl*b"));
  }

  @Test
  public void escaped_star_is_unchanged() throws Exception {
    assertEquals("gl\\*b", Util.convertGlobToRegex("gl\\*b"));
  }

  @Test
  public void question_mark_becomes_dot() throws Exception {
    assertEquals("gl.b", Util.convertGlobToRegex("gl?b"));
  }

  @Test
  public void escaped_question_mark_is_unchanged() throws Exception {
    assertEquals("gl\\?b", Util.convertGlobToRegex("gl\\?b"));
  }

  @Test
  public void character_classes_dont_need_conversion() throws Exception {
    assertEquals("gl[-o]b", Util.convertGlobToRegex("gl[-o]b"));
  }

  @Test
  public void escaped_classes_are_unchanged() throws Exception {
    assertEquals("gl\\[-o\\]b", Util.convertGlobToRegex("gl\\[-o\\]b"));
  }

  @Test
  public void negation_in_character_classes() throws Exception {
    assertEquals("gl[^a-n!p-z]b", Util.convertGlobToRegex("gl[!a-n!p-z]b"));
  }

  @Test
  public void nested_negation_in_character_classes() throws Exception {
    assertEquals("gl[[^a-n]!p-z]b", Util.convertGlobToRegex("gl[[!a-n]!p-z]b"));
  }

  @Test
  public void escape_carat_if_it_is_the_first_char_in_a_character_class() throws Exception {
    assertEquals("gl[\\^o]b", Util.convertGlobToRegex("gl[^o]b"));
  }

  @Test
  public void metachars_are_escaped() throws Exception {
    assertEquals("gl..*\\.\\(\\)\\+\\|\\^\\$\\@\\%b", Util.convertGlobToRegex("gl?*.()+|^$@%b"));
  }

  @Test
  public void metachars_in_character_classes_dont_need_escaping() throws Exception {
    assertEquals("gl[?*.()+|^$@%]b", Util.convertGlobToRegex("gl[?*.()+|^$@%]b"));
  }

  @Test
  public void escaped_backslash_is_unchanged() throws Exception {
    assertEquals("gl\\\\b", Util.convertGlobToRegex("gl\\\\b"));
  }

  @Test
  public void slashQ_and_slashE_are_escaped() throws Exception {
    assertEquals("\\\\Qglob\\\\E", Util.convertGlobToRegex("\\Qglob\\E"));
  }

  @Test
  public void braces_are_turned_into_groups() throws Exception {
    assertEquals("(glob|regex)", Util.convertGlobToRegex("{glob,regex}"));
  }

  @Test
  public void escaped_braces_are_unchanged() throws Exception {
    assertEquals("\\{glob\\}", Util.convertGlobToRegex("\\{glob\\}"));
  }

  @Test
  public void commas_dont_need_escaping() throws Exception {
    assertEquals("(glob,regex),", Util.convertGlobToRegex("{glob\\,regex},"));
  }
}
