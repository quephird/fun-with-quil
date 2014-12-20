(ns fun-with-quil.animations.moire
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (let [w (q/width)
        h (q/height)]
    (q/smooth)
    (q/color-mode :hsb)
    (q/ellipse-mode :center)
    (q/no-fill)
    (q/background 35 255 255)
    (q/no-loop)
    ))

(defn rings [x y]
  (let [sw 30]
    (q/push-matrix)
    (q/translate x y)
    (q/stroke 15 255 255 127)
    (q/stroke-weight 8)
    (q/point 0 0)
    (dotimes [i sw]
      (q/stroke 180 255 255)
      (q/stroke-weight (* 0.25 (- sw i)))
      (q/ellipse 0 0 (* i sw) (* i sw))
      (q/stroke (q/map-range i 0 sw 15 35) 255 255 (q/map-range i 0 (dec sw) 127 0))
      (q/stroke-weight (* 0.25 (+ sw i)))
      (q/ellipse 0 0 (* (+ i 0.5) sw) (* (+ i 0.5) sw)))
    (q/pop-matrix)))

(defn draw []
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)]
    (dotimes [_ 7]
      (rings (q/random w) (q/random h)))
    (q/save "moire.png")
    ))

(q/sketch
  :title      "moire"
  :size       [1400 800]
  :setup      setup
  :draw       draw)
