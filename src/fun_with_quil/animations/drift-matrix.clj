(ns fun-with-quil.animations.drift-matrix
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn setup []
  (q/no-stroke))

(defn draw []
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        focus-x (* 500 (q/cos (* 0.04 fc)))
        focus-y (* 500 (q/sin (* 0.04 fc)))
        ]
    (q/background 255)
    (q/camera 0 0 0
              focus-x focus-y 0
              0 0 1)

    (q/fill 0)
    (doseq [x (range -1025 1025 50)
            y (range -1025 1025 50)
            z (range -1025 1025 50)]
      (q/push-matrix)
      (q/translate x y z)

      ; This is a total cheat to fake a 3D effect by
      ; rotating the 2D ellipses such that they face the viewer,
      ; thus looking like spheres. Drawing spheres in
      ; Processing is waaaaay too slow because of the number
      ; of triangles drawn to construct a convincing
      ; shape.
      (q/rotate-z (q/atan (/ y x)))
      (q/rotate-y (+ q/HALF-PI (q/atan (/ z (q/sqrt (+ (* x x) (* y y)))))))
      (q/ellipse 0 0 2 2)
      (q/pop-matrix))))


(q/defsketch drift-matrix
  :size      [800 800]
  :title      "drift matrix"
  :renderer   :opengl
  :setup      setup
  :draw       draw)
