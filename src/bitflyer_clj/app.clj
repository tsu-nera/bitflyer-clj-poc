(ns bitflyer-clj.app
  (:require
   [bitflyer-clj.lib :as lib]))

(def position (atom "none"))

(def spread-entry 0.0005)
(def spread-cancel 0.0003)

(defn exec-none->entry! []
  (let [{:keys [spread ask bid]} (lib/get-eff-tick)]
    (if (> spread))
    (println ask bid)))

(defn exec-entry->none! []
  (println "entry to none")
  (println (lib/get-eff-tick)))

(defn update-position [current]
  (if (= current "none")
    "entry"
    "none"))

(defn exec [time]
  (if (= @position "none")
    (exec-none->entry!)
    (exec-entry->none!)))
