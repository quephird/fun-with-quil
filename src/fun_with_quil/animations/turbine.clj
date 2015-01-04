(ns fun-with-quil.animations.turbine
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (q/background 0)
  (q/no-stroke))

(defn draw []
  (let [θ           (* 0.5 (q/frame-count))
        w           (q/width)
        h           (q/height)
        center-x    (* w 0.5)
        center-y    (* h 0.5)
        wedge-groups 12
        colors      [[127 0 255]
                     [127 0 192]
                     [127 0 127]
                     [63 0 63]
                     [0 0 0]]
        dθ          (/ 360 wedge-groups (count colors))
        wedge-r     (* 0.5 (q/sqrt (+ (* w w) (* h h))))
        wedge-x     (* 0.5 wedge-r (q/sin (* dθ 0.5)))
        disc-d      (* wedge-r 0.25)]
    (q/translate center-x center-y)
    (q/rotate (q/radians θ))

    (q/push-matrix)
    (doseq [_ (range wedge-groups)]
      (doseq [slice-color colors]
        (apply q/fill slice-color)
        (q/begin-shape :triangles)
        (q/vertex 0 0)
        (q/vertex (- wedge-x) (- wedge-r))
        (q/vertex wedge-x (- wedge-r))
        (q/end-shape)
        (q/rotate (q/radians dθ))))
    (q/pop-matrix)

    (q/fill 0)
    (q/ellipse 0 0 disc-d disc-d)))

(q/sketch
  :title "turbine"
  :setup setup
  :draw draw
  :size [800 800])
