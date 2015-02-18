(ns fun-with-quil.animations.box-fractal-cluster
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (q/smooth)
  (q/stroke 255)
  (q/fill 0)
  (q/color-mode :hsb))

; TODO: Need to fix color problem
(defn cross-iter [s n max-n]
  (let [fc (q/frame-count)
        θ  (* 1.3 fc (if (even? n) -1 1))
        h  (mod (+ fc (q/map-range n 0 max-n 0 255)) 255)]
    (doseq [[x y] [[(- q/HALF-PI) 0]
                   [q/HALF-PI 0]
                   [0 0]
                   [0 q/HALF-PI]
                   [0 q/PI]
                   [0 (- q/HALF-PI)]]]
      (q/push-matrix)
      (q/rotate-x x)
      (q/rotate-y y)
      (q/translate 0 0 s)
      (q/stroke h 255 255)
      (q/box s)
      (if (and (not (zero? n)))
        (do
          (q/push-matrix)
          (q/rotate-z (q/radians θ))
          ; TODO: Need to relate computation of translation below
          ;       with next scale when recursing.
          (q/translate 0 0 (* 1.25 s))
          (cross-iter (* 0.5 s) (dec n) max-n)
          (q/pop-matrix)))
      (q/pop-matrix))))

(defn cross [s n]
  (cross-iter s n n))

(defn draw []
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)]
    (q/background 0)
    (q/camera (* 2000 (q/sin (* 0.01 fc))) (* 2000 (q/cos (* 0.01 fc))) 0
              0 0 0
              0 0 1)
    (q/translate 0 0 0)
    (cross 200 3)))

(q/defsketch box-fractal-cluster
  :title "box fractal cluster"
  :setup setup
  :draw draw
  :renderer :p3d
  :size [1000 1000])
