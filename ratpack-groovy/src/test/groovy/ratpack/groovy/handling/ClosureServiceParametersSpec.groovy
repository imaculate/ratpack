/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.groovy.handling

import com.google.inject.AbstractModule
import ratpack.test.internal.RatpackGroovyAppSpec

class ClosureServiceParametersSpec extends RatpackGroovyAppSpec {

  static class Thing {}

  static class ThingModule extends AbstractModule {
    protected void configure() {
      bind(Thing)
    }
  }

  def "can have global services"() {
    when:
    file "templates/foo.html", "bar"

    modules {
      register(new ThingModule())
    }

    handlers { Thing thing ->
      get {
        response.send thing.class.name
      }
    }

    then:
    text == Thing.class.name
  }

}
