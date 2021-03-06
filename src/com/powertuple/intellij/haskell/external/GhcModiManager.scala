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

package com.powertuple.intellij.haskell.external

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project

object GhcModiManager {

  private var restart = false

  def doRestart() {
    restart = true
  }

  def getGhcModi(project: Project): GhcModi = {
    if (restart) {
      val ghcModi = ServiceManager.getService(project, classOf[GhcModi])
      ghcModi.exit()
      ghcModi
    } else {
      ServiceManager.getService(project, classOf[GhcModi])
    }
  }
}
