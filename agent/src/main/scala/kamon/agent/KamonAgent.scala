/* =========================================================================================
 * Copyright © 2013-2016 the kamon project <http://kamon.io/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 * =========================================================================================
 */

package kamon.agent

import java.lang.instrument.Instrumentation
import kamon.api.instrumentation.logger.LazyLogger
import kamon.agent.util.Utils._

object KamonAgent {

  val log = LazyLogger("KamonAgent")

  /**
   * JVM hook to statically load the javaagent at startup.
   *
   * After the Java Virtual Machine (JVM) has initialized, the premain method
   * will be called. Then the real application main method will be called.
   *
   * @param args
   * @param instrumentation
   * @throws Exception
   */
  @throws(classOf[Exception])
  def premain(args: String, instrumentation: Instrumentation): Unit = {
    withTimeSpent(InstrumentationLoader.load(instrumentation)) {
      timeSpent ⇒
        log.info(s"Premain startup complete in $timeSpent ms");
    }
  }
  /**
   * JVM hook to dynamically load javaagent at runtime.
   *
   * The agent class may have an agentmain method for use when the agent is
   * started after VM startup.
   *
   * @param args
   * @param instrumentation
   * @throws Exception
   */
  @throws(classOf[Exception])
  def agentmain(args: String, instrumentation: Instrumentation): Unit = {
    log.debug(s"agentmain method invoked with args: $args and inst: $instrumentation")
    premain(args, instrumentation)
  }
}