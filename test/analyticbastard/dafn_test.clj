(ns analyticbastard.dafn-test
  (:require [clojure.test :refer :all]
            [analyticbastard.dafn :refer [dafn]]))

(deftest basic-test
  (testing "Testing no arguments and constant body"
    (let [output "constant output"]
      (dafn basic-test-async-fn []
            output)
      (is (= output 
             @(basic-test-async-fn))))))

(deftest one-param-test
  (let [input 1]
    (testing "Testing no arguments and constant body"
      (dafn one-param-test-async-fn [a]
            a)
      (is (= input
             @(one-param-test-async-fn input))))
    (testing "Testing no arguments and constant body"
      (dafn one-param-process-test-async-fn [a]
            (inc a))
      (is (= (inc input)
             @(one-param-process-test-async-fn input))))))

(deftest two-param-test
  (testing "Testing no arguments and constant body"
    (let [b {:x 10}
          output [1 (:x b)]]
      (dafn deconstruct-test-async-fn [a b]
            (let [{:keys [x]} b] 
              [a x]))
      (is (= output
             @(deconstruct-test-async-fn 1 b)))
      (is (= output
             @(deconstruct-test-async-fn 1 {:x (second output)}))))))

