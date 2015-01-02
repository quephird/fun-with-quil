(ns fun-with-quil.animations.acid-rain
  (:require [quil.core :as q :include-macros :true]))

(defn setup []
  (q/no-stroke)
  (q/background 0))

(defn draw []
  (let [w      (q/width)
        h      (q/height)
        possible-colors [[255 0 0] [0 255 0] [0 0 255]]
        drop-c (possible-colors (rand-int (count possible-colors)))
        drop-w (+ 25 (q/random 75))
        drop-x (q/random w)
        drop-y (q/random h)]
    (q/fill 0 0 0 5)
    (q/rect 0 0 w h)
    (q/push-matrix)
    (q/display-filter :blur 1.0)
    (q/translate drop-x drop-y)
    (apply q/fill drop-c)
    (q/ellipse 0 0 drop-w drop-w)
    (q/pop-matrix)))

(q/sketch
  :title "acid rain"
  :setup setup
  :draw draw
  :size [1440 800])
