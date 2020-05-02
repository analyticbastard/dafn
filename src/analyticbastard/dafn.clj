(ns analyticbastard.dafn
  (:require [clojure.core.async :as async]))

(def ch (async/chan 1))
(def sub-c (async/pub ch :route))

(defmacro dafn [fname args & body]
  (let [values (gensym 'values)
        fun-name (str fname)
        d2 `(defn ~fname [~@args]
              (let [p# (promise)]
                (println ~fname (keyword ~fname))
                (async/go (async/>!! ch 
                                     {:route ~fname :data (cons p# ~args)}))
                p#))
        d3 `(async/go-loop []
              (let [in-ch# (async/chan 1)
                    _# (async/sub sub-c ~fname in-ch#)
                    recv# (:data (async/<! in-ch#))
                    p# (first recv#)
                    ~values (rest recv#)]
                (println "xxx" ~fname  ~values)
                (deliver p#
                         (let ~(into [] (interleave args
                                                    (map-indexed (fn [i _] `(get ~values ~i)) args)))
                           (println "body" ~body ~@body)
                           ~@body)))
              (recur))]
    [d2 d3]))

