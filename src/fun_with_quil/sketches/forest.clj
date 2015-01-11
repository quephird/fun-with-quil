(ns fun-with-quil.sketches.forest
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn setup []
  (q/smooth)
  (q/no-stroke)
  (q/no-loop))

(defn tapered-spiral-fn [t p]
  [(* (+ (* t 10) (* 12 (q/sqrt t) (q/cos p))) (q/cos t))
   (* (+ (* t 10) (* 12 (q/sqrt t) (q/cos p))) (q/sin t))
   (+ (* 50 t) (* 12 (q/sqrt t) (q/sin p)))])

(defn tapered-spiral [x y c]
  (q/push-matrix)
  (q/translate x y 0)
    (apply q/fill c)
    (let [dt (/ q/TWO-PI 50)
          dp (/ q/TWO-PI 20)]
      (doseq [t (range 0 (* 6 q/TWO-PI) dt)]
        (doseq [p (range 0 q/TWO-PI dp)]
          (q/begin-shape :quads)
          (doseq [angle [[t p]
                         [(+ t dt) p]
                         [(+ t dt) (+ p dp)]
                         [t (+ p dp)]]]
            (apply q/vertex (apply tapered-spiral-fn angle)))
          (q/end-shape))))
  (q/pop-matrix))

(defn draw [forest]
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        cs [[255 127 0]
            [245 222 179]
            [255 255 0]
            [0 255 0]
            [135 206 250]
            [127 0 255]]]
    (q/background 0)
    (q/translate (* 0.5 w) (* 0.5 h))

    (q/camera 0 2500 -200
              0 0 750
              0 0 1)
    (q/point-light 168 168 168
                   0 2000 -1000)

    (dotimes [_ 20]
      (let [c (cs (rand-int (count cs)))
            x (q/random -3000 3000)
            y (q/random -3000 0)]
        (tapered-spiral x y c)))
    (q/save "forest.png")
    )
  )

(q/defsketch forest
  :title      "forest"
  :size       [1440 800]
  :renderer   :opengl
  :setup      setup
  :draw       draw
  :middleware [m/fun-mode])
