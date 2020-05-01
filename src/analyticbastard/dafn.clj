(ns analyticbastard.dafn
  (:require [clojure.core.async :as async]))

(def in-ch (async/chan 1))

(defmacro dafn [fname args & body]
  (let [d2 `(defn ~fname [~@args]
              (let [p# (promise)]
                (println p#)
                (async/go (async/>!! user/in-ch p#))
                (loop [args# ~args]
                  (let [v# (first args#)]
                    (when v#
                      (println "sent" v#)
                      (async/go (async/>!! user/in-ch v#))
                      (recur (rest args#)))))
                p#))
        d3 `(async/go-loop []
              (println "start")
              (let [p# (async/<! user/in-ch)]
                (deliver p#
                         (let ~(into [] (zipmap args
                                                    (map (fn [_] '(async/<! user/in-ch)) args)))
                           (println "received" ~@args)
                           (println ~(str args) ~args ~(str body))
                           ~@body)))
              (recur))]
    [d2 d3]))

