(ns bitflyer-clj.api
  (:require
   [bitflyer-clj.tool :as tool]
   [camel-snake-kebab.core :as csk]
   [camel-snake-kebab.extras :as cske]
   [clj-http.client :as client])
  (:import
   (java.time
    Instant)))

(def product-code "FX_BTC_JPY")
(def base-params {"product_code" product-code})

(def base-url "https://api.bitflyer.com")
(defn ->url [path] (str base-url path))

(def creds-file "creds.edn")
(def creds (tool/load-edn creds-file))

(defn ->timestamp []
  (.getEpochSecond (Instant/now)))

(defn ->access-sign [timestamp method path]
  (let [secret (:api-secret creds)
        text   (str timestamp method path)]
    (tool/sign secret text)))

(defn ->signed-headers [method path body]
  (let [key       (:api-key creds)
        timestamp (->timestamp)
        sign      (->access-sign timestamp method path)]
    {"ACCESS-KEY"       key
     "ACCESS-TIMESTAMP" timestamp
     "ACCESS-SIGN"      sign}))

(defn get-public [path]
  (let [url     (->url path)
        options {:as            :json
                 :query-params  base-params
                 :cookie-policy :standard}]
    (when-let [resp (client/get url options)]
      (->> resp
           :body
           (cske/transform-keys csk/->kebab-case)))))

(defn get-private [path]
  (let [headers (->signed-headers "GET" path nil)
        url     (->url path)]
    (when-let [resp (client/get url
                                {:headers       headers
                                 :as            :json
                                 :cookie-policy :standard})]
      (->> resp
           :body
           (cske/transform-keys csk/->kebab-case)))))

(defn fetch-markets
  []
  (get-public "/v1/markets"))
#_(fetch-markets)

(defn fetch-order-book
  []
  (get-public "/v1/board"))
#_(fetch-order-book)

(defn fetch-tick
  []
  (get-public "/v1/ticker"))
#_(fetch-tick)

;; 何度も叩くと取得できる. よくわからない...
(defn fetch-balance
  "資産残高を取得"
  []
  (get-private "/v1/me/getbalance"))
#_(fetch-balance)

(defn fetch-collateral
  "証拠金の状態を取得"
  []
  (get-private "/v1/me/getcollateral"))
#_(fetch-collateral)

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

(comment

  (def ts (->timestamp))
  (->access-sign ts "GET" "/v1/me/getbalance")

  )
