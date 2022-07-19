(ns bitflyer-clj.lib
  (:require
   [bitflyer-clj.api :as api]))

(defn get-asset
  "JPY残高を参照する関数"
  [])

(defn get-colla
  "JPY証拠金を参照する関数"
  [])

#_(defn get-eff-ticker
    "板情報から実効Ask/Bid(=指値を入れる基準値)を計算する関数"
    [])

(def th-size 0.01)

(defn ->limit-price
  "TODO 板情報から実効bid/askを計算"
  [board]
  (-> board first :price))

(defn market
  "成行注文"
  [side size])

(defn limit
  "指値注文"
  [side size price])

(defn cancel
  "注文キャンセル"
  [id])

(defn get-status
  "注文ステータス取得"
  [id])

(comment

  (def order-book (api/fetch-order-book))

  (def bids (:bids order-book))
  (count bids)
  (->limit-price bids)

  (def asks (:asks order-book))
  (count asks)
  (->limit-price bids)

  )
