# dafn

Define asynchronous functions, just like any common function

```clojure
(dafn limited-resource [params]
    (process-and-get-status))
```

`limited-resource` will return a promise that will be realized when the body
is processed for its call.

```clojure
(let [p (limited-resource x)]
    (some-other-process)
    (when (realized? p)
        (more-processing @p)))
```

`limited-resource` can be called from several threads concurrently

## Limitations

As a macro, this comes with certain limitations. In particular
- No variable arguments allowed
- You cannot deconstruct in the argument definition 

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
