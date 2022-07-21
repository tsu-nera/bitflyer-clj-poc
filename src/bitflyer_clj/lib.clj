(ns bitflyer-clj.lib
  (:require
   [bitflyer-clj.api :as api]))

(def lot 0.001)

(defn ->limit-price
  "TODO 板情報から実効bid/askを計算"
  [board]
  (-> board first :price))

(def th-size 0.01)

(defn ->spread [bid ask]
  (/ (- ask bid) bid))

(defn get-eff-tick
  "板情報から実効Ask/Bid(=指値を入れる基準値)を計算する関数"
  []
  (let [order-book (api/fetch-order-book)
        ask        (-> order-book :asks ->limit-price)
        bid        (-> order-book :bids ->limit-price)
        spread     (->spread bid ask)]
    {:ask ask :bid bid :spread spread}))
#_(get-eff-tick)

(defn get-order [id]
  (when-let [resp (api/fetch-orders {"child_order_acceptance_id" id})]
    (first resp)))
#_(get-order "JRF20220720-110046-022548")

(defn- ->status [child-order-state]
  (case child-order-state
    "ACTIVE"    "OPEN"
    "COMPLETED" "CLOSED"
    :else       child-order-state))

(defn get-status [id]
  (when-let [order (get-order id)]
    (let [filled    (:executed-size order)
          amount    (:size order)
          remaining (- amount filled)]
      {:id        id
       :status    (->status (:child-order-state order))
       :price     (:price order)
       :amount    amount
       :filled    filled
       :remaining remaining})))
#_(get-status "JRF20220720-110046-022548")

(defn buy-market-order [amount price]
  (api/create-order
   {"child_order_type" "MARKET"
    "side"             "BUY"
    "size"             amount
    "price"            price}))

(defn sell-market-order [amount price]
  (api/create-order
   {"child_order_type" "MARKET"
    "side"             "SELL"
    "size"             amount
    "price"            price}))

(defn buy-limit-order [amount price]
  (api/create-order
   {"child_order_type" "LIMIT"
    "side"             "BUY"
    "size"             amount
    "price"            price}))

(defn sell-limit-order [amount price]
  (api/create-order
   {"child_order_type" "LIMIT"
    "side"             "SELL"
    "size"             amount
    "price"            price}))

(comment

  (def order-book (api/fetch-order-book))

  (def bids (:bids order-book))
  (count bids)
  ((->limit-price bids))

  (def asks (:asks order-book))
  (count asks)
  (->limit-price asks)

  ;;
  (def sample-bid (->limit-price bids))
  (def resp (buy-limit-order lot sample-bid))
  (def sample-ask (->limit-price asks))
  (def resp (sell-limit-order lot sample-ask))

  (def order-id (:child-order-acceptance-id resp))
  (get-order order-id)

  (def status (get-status order-id))
  (api/cancel-order order-id)
  )
