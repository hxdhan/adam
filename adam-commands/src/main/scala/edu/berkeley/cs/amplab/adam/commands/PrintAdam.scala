/*
 * Copyright (c) 2013. Regents of the University of California
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.berkeley.cs.amplab.adam.commands

import org.apache.avro.generic.IndexedRecord
import scala.collection.JavaConversions._
import edu.berkeley.cs.amplab.adam.util.{ParquetFileTraversable, Args4jBase, Args4j}
import org.kohsuke.args4j.Argument
import java.util

object PrintAdam {
  def main(args: Array[String]) {
    new PrintAdam().commandExec(args)
  }
}

class PrintAdamArgs extends Args4jBase with SparkArgs {
  @Argument(required = true, metaVar = "FILE(S)", multiValued = true, usage = "One or more files to print")
  var filesToPrint = new util.ArrayList[String]()
}

class PrintAdam extends AdamCommand with SparkCommand {
  val commandName: String = "print"
  val commandDescription: String = "Print an ADAM formatted file"

  def commandExec(cmdArgs: Array[String]) {
    val args = Args4j[PrintAdamArgs](cmdArgs)
    val sc = createSparkContext(args)

    for (file <- args.filesToPrint) {
      val it = new ParquetFileTraversable[IndexedRecord](sc, file)
      for (pileup <- it) {
        println(pileup)
      }
    }
  }
}
