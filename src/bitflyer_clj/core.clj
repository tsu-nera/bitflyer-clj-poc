(ns bitflyer-clj.core
  (:require
    [camel-snake-kebab.core :as csk]
    [camel-snake-kebab.extras :as cske]
    [clj-http.client :as client]))


(def end-point "https://api.bitflyer.com/v1/")


(defn get-ticker
  []
  (when-let [resp (client/get (str end-point "ticker")
                              {:as            :json
                               :cookie-policy :standard})]
    (->> resp
         :body
         (cske/transform-keys csk/->kebab-case))))


(comment
  ;;
  (def ticker (get-ticker))
  (println (get-ticker))

  (dotimes [_ 3]
    (println (get-ticker))
    (Thread/sleep 1000))
  ;;
  )
