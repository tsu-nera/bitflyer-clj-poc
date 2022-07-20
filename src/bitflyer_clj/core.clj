(ns bitflyer-clj.core
  (:require
   [bitflyer-clj.lib :as lib]))

;; 実効スプレッド(100%=1,1%=0.01)この値を上回ったらentry
(def spread-entry-th 0.0005)
;; 実効スプレッド (100%=1,1%=0.01) がこの値を下回ったら指値更新を停止
(def spread-cancel-th 0.0003)
;;  実効Ask/BidからDELTA離れた位置に指値をおく.
(def delta 1)

(def position (atom nil))

(comment

  (let [tick   (lib/get-eff-tick)
        spread (:spread tick)]
    (println tick)
    (if (> spread spread-entry-th)
      "entry"
      "not entry"))

  )

(comment

  position
  @position

  )
