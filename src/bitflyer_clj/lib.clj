(ns bitflyer-clj.lib)

(defn get-asset
  "JPY残高を参照する関数"
  [])

(defn get-colla
  "JPY証拠金を参照する関数"
  [])

(defn get-effective-tick
  "板情報から実効Ask/Bid(=指値を入れる基準値)を計算する関数"
  [])

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
