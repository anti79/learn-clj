(ns learnclj.frontend.utils.localstorage
  (:require [cljs.tools.reader :refer [read-string]]))
(defn set-item!
  [key val]
  (.setItem (.-localStorage js/window) key val))

(defn get-item
  [key]
  (.getItem (.-localStorage js/window) key))

(defn remove-item!
  [key]
  (.removeItem (.-localStorage js/window) key))

(defn get-all []
  (let [local-storage js/localStorage
        keys (js/Object.keys local-storage)
        items (into {} (map (fn [key]
                              [(keyword key) (.getItem local-storage key)])
                            keys))]
    items))

(defn add-to-set! [key val]
  (let [current-vec (get-item key)
        new-vec (conj (read-string current-vec) val)]

    (set-item! key new-vec)))

(defn remove-from-set! [key val]
  (let [current-vec (get-item key)
        new-vec (disj (read-string current-vec) val)]

    (set-item! key new-vec)))

(defn get-set [key]
  (read-string (get-item key)))
