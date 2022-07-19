(ns bitflyer-clj.api
  (:require
   [camel-snake-kebab.core :as csk]
   [camel-snake-kebab.extras :as cske]
   [clj-http.client :as client]
   [clojure.edn :as edn]
   [clojure.java.io :as io]))

(def product-code "FX_BTC_JPY")
(def base-params {"product_code" product-code})

(def base-url "https://api.bitflyer.com/v1")
(defn ->url [end-point]
  (str base-url end-point))

(defn load-edn [file-path]
  (when-let [file (io/resource file-path)]
    (-> file
        slurp
        edn/read-string)))

(def creds-file "creds.edn")
(def creds (load-edn creds-file))

(defn public [url]
  (when-let [resp (client/get url
                              {:as            :json
                               :query-params  base-params
                               :cookie-policy :standard})]
    (->> resp
         :body
         (cske/transform-keys csk/->kebab-case))))

(defn fetch-markets
  []
  (public (->url "/markets")))
#_(fetch-markets)

(defn fetch-order-book
  []
  (public (->url "/board")))
#_(fetch-order-book)

(defn fetch-tick
  []
  (public (->url "/ticker")))
#_(fetch-tick)

(defn create-order [type side amount price]
  (let [url          (->url "/me/sendchildorder")
        query-params (merge base-params
                            {"child_order_type" type
                             "side"             side
                             "size"             amount
                             "price"            price})]
    query-params))

(defn cancel-order [id symbol & params])

(comment
  ;;
  (dotimes [_ 3]
    (println (fetch-tick))
    (Thread/sleep 1000))
  ;;
  )

