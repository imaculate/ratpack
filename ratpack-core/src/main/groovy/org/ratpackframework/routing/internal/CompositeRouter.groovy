/*
 * Copyright 2012 the original author or authors.
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

package org.ratpackframework.routing.internal

import org.ratpackframework.routing.Router
import org.vertx.java.core.Handler
import org.vertx.java.core.http.HttpServerRequest

class CompositeRouter implements Router {

  List<Router> routers

  CompositeRouter(List<Router> routers) {
    this.routers = routers
  }

  @Override
  void handle(RoutedRequest routedRequest) {
    final routersCopy = new ArrayList<Router>(routers)
    tryNext(routersCopy, routedRequest, routedRequest.notFoundHandler)
  }

  private void tryNext(List<Router> remainingRouters, RoutedRequest routedRequest, Handler<HttpServerRequest> exhaustedHandler) {
    if (remainingRouters.empty) {
      exhaustedHandler.handle(routedRequest.request)
    } else {
      Router router = remainingRouters.remove(0)
      router.handle(routedRequest.withNotFoundHandler(new Handler<HttpServerRequest>() {
        @Override
        void handle(HttpServerRequest ignore) {
          tryNext(remainingRouters, routedRequest, exhaustedHandler)
        }
      }))
    }
  }

  int hashCode() {
    return routers.hashCode()
  }

  boolean equals(o) {
    if (this.is(o)) {
      return true
    }
    if (getClass() != o.class) {
      return false
    }

    CompositeRouter that = (CompositeRouter) o

    if (routers != that.routers) {
      return false
    }

    return true
  }

}