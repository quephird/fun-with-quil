(ns fun-with-quil.animations.fade-and-emerge
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (q/smooth)
;  (q/no-loop)
  )

(defn draw []
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)]
    (q/no-stroke)
    (q/fill 0 5)
    (q/rect 0 0 w h)
    (q/translate (* 0.5 w) (* 0.5 h))
    (q/rotate (* 0.05 fc))
    (q/fill 255)
    (q/begin-shape)
    (q/vertex 0 0)
    (q/vertex -20 (* 0.71 h))
    (q/vertex 20 (* 0.71 h))
    (q/end-shape :close)
    )
  )

(q/defsketch fade-and-emerge
  :title "fade and emerge"
  :setup setup
  :draw draw
  :size [800 800])
