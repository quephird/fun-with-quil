(ns fun-with-quil.sketches.acid-rain
  (:use quil.core))

(def screen-w 1920)
(def screen-h 1080)

(defn setup []
  (smooth)
  (no-stroke)
  (background 0))

(defn draw []
  (let [possible-colors [[255 0 0] [0 255 0] [0 0 255]]
        drop-c (possible-colors (rand-int (count possible-colors)))
        drop-w (+ 25 (random 75))
        drop-x (random screen-w)
        drop-y (random screen-h)]
    (fill 0 0 0 5)
    (rect 0 0 screen-w screen-h)
    (push-matrix)
    (display-filter :blur 0.6)
    (translate drop-x drop-y)
    (apply fill drop-c)
    (ellipse 0 0 drop-w drop-w)
    (pop-matrix)))

(sketch
  :title "acid rain"
  :setup setup
  :draw draw
  :renderer :java2d
  :size [screen-w screen-h])
