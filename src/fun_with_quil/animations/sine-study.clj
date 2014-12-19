(ns fun-with-quil.animations.sine-study
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (let [w (q/width)
        h (q/height)]
    (q/smooth)
    (q/stroke-weight 2)
    (q/no-fill)
    (q/color-mode :hsb)
    ))

(defn wave-set [wave-count]
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)]
    (dotimes [i wave-count]
      (let [y     (q/map-range i 0 (dec wave-count) (* -0.5 h) (* 0.5 h))
            phase (* 2 fc (if (even? i) 1 -1))
            hue   (mod (+ (* i 10) fc) 255)]
        (q/stroke hue 255 255)
        (q/begin-shape)
        (doseq [x (range (* -0.5 w) (* 0.5 w) 5)]
          (q/vertex x (+ y (* 50 (q/sin (q/radians (+ phase x)))))))
        (q/end-shape)))))

(defn draw []
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)]
    (q/translate (* 0.5 w) (* 0.5 h))
    (q/background 0)
    (wave-set 13)
    (q/rotate q/HALF-PI)
    (wave-set 13)))

(q/sketch
  :title      "sine study"
  :size       [800 800]
  :setup      setup
  :draw       draw)
