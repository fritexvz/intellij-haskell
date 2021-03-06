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

package com.powertuple.intellij.haskell.psi

import com.intellij.lang.ASTNode
import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiFileFactory, PsiWhiteSpace}
import com.powertuple.intellij.haskell.util.OSUtil
import com.powertuple.intellij.haskell.{HaskellFile, HaskellLanguage}

object HaskellElementFactory {
  def createQvarId(project: Project, name: String): ASTNode = {
    val haskellFile = createFileFromText(project, name)
    PsiTreeUtil.findChildOfType(haskellFile, classOf[HaskellQvarId]).getNode
  }

  def createQconId(project: Project, name: String): ASTNode = {
    val haskellFile = createFileFromText(project, name)
    PsiTreeUtil.findChildOfType(haskellFile, classOf[HaskellQconId]).getNode
  }

  def createQvarSym(project: Project, name: String): ASTNode = {
    val haskellFile = createFileFromText(project, name)
    PsiTreeUtil.findChildOfType(haskellFile, classOf[HaskellQvarSym]).getNode
  }

  def createGconSym(project: Project, name: String): ASTNode = {
    val haskellFile = createFileFromText(project, name)
    PsiTreeUtil.findChildOfType(haskellFile, classOf[HaskellGconSym]).getNode
  }

  def createExpression(project: Project, expression: String): HaskellExpression = {
    val haskellFile = createFileFromText(project, expression)
    PsiTreeUtil.findChildOfType(haskellFile, classOf[HaskellExpression])
  }

  def createTopDeclaration(project: Project, declaration: String): HaskellTopDeclaration = {
    val haskellFile = createFileFromText(project, declaration)
    PsiTreeUtil.findChildOfType(haskellFile, classOf[HaskellTopDeclaration])
  }

  def createLanguagePragma(project: Project, languagePragma: String): HaskellLanguagePragma = {
    val haskellFile = createFileFromText(project, languagePragma)
    PsiTreeUtil.findChildOfType(haskellFile, classOf[HaskellLanguagePragma])
  }

  def createWhiteSpace(project: Project) = {
    val haskellFile = createFileFromText(project, " ")
    PsiTreeUtil.findChildOfType(haskellFile, classOf[PsiWhiteSpace])
  }

  def createNewLine(project: Project) = {
    createFileFromText(project, OSUtil.LineSeparator).getFirstChild
  }

  private def createFileFromText(project: Project, text: String): HaskellFile = {
    PsiFileFactory.getInstance(project).createFileFromText("a.hs", HaskellLanguage.Instance, text).asInstanceOf[HaskellFile]
  }
}
