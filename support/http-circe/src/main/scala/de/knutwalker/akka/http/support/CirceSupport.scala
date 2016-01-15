/*
 * Copyright 2015 – 2016 Paul Horn
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

package de.knutwalker.akka.http.support

import de.knutwalker.akka.http.JsonSupport
import de.knutwalker.akka.stream.support.circe

import akka.http.scaladsl.marshalling.{ Marshaller, ToEntityMarshaller }
import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller

import io.circe.jawn.CirceSupportParser._
import io.circe.{ Decoder, Encoder, Json, Printer }

import scala.language.implicitConversions


object CirceSupport extends CirceSupport

trait CirceSupport extends JsonSupport {

  implicit def circeJsonUnmarshaller: FromEntityUnmarshaller[Json] =
    jsonUnmarshaller[Json]

  implicit def circeUnmarshaller[A: Decoder]: FromEntityUnmarshaller[A] =
    circeJsonUnmarshaller.map(circe.decodeJson[A])

  implicit def circeJsonMarshaller(implicit P: Printer = Printer.noSpaces): ToEntityMarshaller[Json] =
    Marshaller.StringMarshaller.wrap(`application/json`)(P.pretty)

  implicit def circeMarshaller[A](implicit A: Encoder[A], P: Printer = Printer.noSpaces): ToEntityMarshaller[A] =
    circeJsonMarshaller.compose(A.apply)
}
