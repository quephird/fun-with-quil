(ns wobble
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (q/no-stroke)
  (q/frame-rate 30))

(defn draw []
  (let [w      (q/width)
        h      (q/height)
        fc     (q/frame-count)
        number-of-slices 50
        radians-per-slice (q/radians (/ 360 number-of-slices))
        lobes   5
        colors [[238 214 154]
                [251 107 98]
                [228 52 72]
                [140 42 56]
                [50 37 53]]]
    (q/background 0)
    (q/translate (* 0.5 w) (* 0.5 h))
    (dotimes [i number-of-slices]
      (apply q/fill (colors (rem i (count colors))))
      (q/begin-shape :quads)
      (let [half-slice-length (+ 50 (* 50 (q/sin (+ (* lobes i radians-per-slice) (* fc 0.2)))))
            slice-center-radius 250
            outer-radius (+ slice-center-radius half-slice-length)
            inner-radius (- slice-center-radius half-slice-length)
            outer-slice-width (* outer-radius radians-per-slice)
            inner-slice-width (* inner-radius radians-per-slice)]
        (q/vertex (* outer-slice-width -0.5) (- outer-radius))
        (q/vertex (* inner-slice-width -0.5) (- inner-radius))
        (q/vertex (* inner-slice-width 0.5) (- inner-radius))
        (q/vertex (* outer-slice-width 0.5) (- outer-radius))
        )
      (q/end-shape)
      (q/rotate radians-per-slice))))

(q/sketch
  :title "wobble"
  :size [800 800]
  :setup setup
  :draw draw)
