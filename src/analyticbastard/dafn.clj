(ns analyticbastard.dafn
  (:require [clojure.core.async :as async]))

(def ch (async/chan 1))
(def sub-c (async/pub ch :route))

(defmacro dafn [fname args & body]
  (let [values (gensym 'values)
        caller `(defn ~fname [~@args]
                  (let [p# (promise)]
                    (async/go (async/>!! ch 
                                         {:route ~fname :data (cons p# ~args)}))
                    p#))
        worker `(async/go-loop []
                  (let [in-ch# (async/chan 1)
                        _# (async/sub sub-c ~fname in-ch#)
                        recv# (:data (async/<! in-ch#))
                        p# (first recv#)
                        ~values (rest recv#)]
                    (deliver p#
                             (let ~(into [] (interleave args
                                                        (map-indexed (fn [i _] `(nth ~values ~i)) 
                                                                     args)))
                               ~@body)))
                  (recur))]
    [caller worker]))

