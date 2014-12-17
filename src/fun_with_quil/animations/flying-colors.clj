(ns fun-with-quil.animations.flying-colors
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (q/smooth)
  (q/color-mode :hsb))

(defn draw []
  (let [w  (q/width)
        h  (q/height)
        fc (q/frame-count)
        hue (mod (* 0.25 fc) 256)
        θ  (* fc 1.5)
        segments 50]
    (q/background 0)
    (q/translate (* w 0.5) (* h 0.5))
    (dotimes [i segments]
      (let [a (* h 0.4)
            segment-θ (+ θ (* i 3))
            segment-x (* a (q/sin (q/radians (* 0.625 segment-θ))) (q/cos (q/radians segment-θ)))
            segment-y (* a (q/sin (q/radians (* 0.625 segment-θ))) (q/sin (q/radians segment-θ)))
            segment-d (* 10 (q/sin (q/radians (/ (* i 180.0) segments))))]
        (q/fill hue 255 255)
        (q/ellipse segment-x segment-y segment-d segment-d)))))

(q/sketch
  :title "flying colors"
  :setup setup
  :draw draw
  :size [800 800])
