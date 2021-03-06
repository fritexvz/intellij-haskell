/*
 * Copyright 2014 Rik van der Kleij
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.powertuple.intellij.haskell.navigate

import com.intellij.lang.cacheBuilder.{WordOccurrence, WordsScanner}
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.util.Processor
import com.powertuple.intellij.haskell.HaskellLexer
import com.powertuple.intellij.haskell.psi.HaskellTypes._
import com.powertuple.intellij.haskell.psi._

import scala.annotation.tailrec

class HaskellFindUsagesProvider extends FindUsagesProvider {

  override def getWordsScanner: WordsScanner = {
    new WordsScanner {
      def processWords(fileText: CharSequence, processor: Processor[WordOccurrence]) {
        val lexer = new HaskellLexer
        lexer.start(fileText)
        processTokens(lexer, fileText, processor)
      }
    }
  }

  @tailrec
  private def processTokens(lexer: HaskellLexer, fileText: CharSequence, processor: Processor[WordOccurrence]) {
    val tokenType = lexer.getTokenType
    if (tokenType != null) {
      if (tokenType == HS_QVARID_ID || tokenType == HS_QCONID_ID || tokenType == HS_QVARSYM_ID || tokenType == HS_GCONSYM_ID) {
        val o: WordOccurrence = new WordOccurrence(fileText, lexer.getTokenStart, lexer.getTokenEnd, WordOccurrence.Kind.CODE)
        processor.process(o)
      }
      lexer.advance()
      processTokens(lexer, fileText, processor)
    }
  }

  override def getType(psiElement: PsiElement): String = {
    psiElement.getNode.getElementType match {
      case HS_QVAR_ID => "variable"
      case HS_QCON_ID => "constructor"
      case HS_QVAR_SYM => "variable operator"
      case HS_GCON_SYM => "constructor operator"
      case _ => psiElement.getText
    }
  }

  override def getDescriptiveName(element: PsiElement): String = {
    val name = element match {
      case hv: HaskellNamedElement => hv.getName
      case _ => element.getText
    }
    Option(name) getOrElse "anonymous"
  }

  override def getHelpId(psiElement: PsiElement): String = null

  override def canFindUsagesFor(psiElement: PsiElement): Boolean = {
    psiElement match {
      case _: HaskellNamedElement => true
      case _ => false
    }
  }

  override def getNodeText(psiElement: PsiElement, useFullName: Boolean): String = {
    val text = psiElement match {
      case hv: HaskellNamedElement => hv.getName
      case _ => psiElement.getText
    }
    Option(text) getOrElse "anonymous"
  }
}
