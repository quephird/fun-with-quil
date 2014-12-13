(ns fun-with-quil.sketches.doppeldecker
  (:require [quil.core :as q :include-macros true]))

(defn graded-triangle [s c]
  (let [tri-h       (* 0.866 s)
        upper-tri-h (* 0.7 tri-h)
        quad-h      (- tri-h upper-tri-h)
        upper-tri-s (* 0.7 s)]
    (q/begin-shape :triangles)
    (doseq [[[x y] c] [[[0 0] [0 0 0]]
                       [[(* -0.5 upper-tri-s) upper-tri-h] [127 127 127]]
                       [[(* 0.5 upper-tri-s) upper-tri-h] [127 127 127]]]]
      (apply q/fill c)
      (q/vertex x y))
    (q/end-shape :close)

    (q/begin-shape :quads)
    (doseq [[[x y] c] [[[(* -0.5 upper-tri-s) upper-tri-h] [127 127 127]]
                       [[(* -0.5 s) tri-h] c]
                       [[(* 0.5 s) tri-h] c]
                       [[(* 0.5 upper-tri-s) upper-tri-h] [127 127 127]]]]
      (apply q/fill c)
      (q/vertex x y))
    (q/end-shape :close)))

(defn setup []
  (q/smooth)
  (q/background 127)
  (q/no-stroke)
  (q/no-loop))

(defn draw []
  (let [w     (q/width)
        h     (q/height)
        cols  15
        rows  8
        min-s 120
        possible-colors [[255 255 255]
                         [255 255 0]
                         [255 127 0]
                         [0 255 0]
                         [0 127 255]]]
    (q/translate 0 (- (/ h rows)))
    (dotimes [_ (inc rows)]
      (q/push-matrix)
      (dotimes [_ (inc cols)]
        (let [dx (q/random (* 0.5 min-s))
              dy (q/random (* 0.5 min-s))
              s (q/random min-s (* 2 min-s))
              c (possible-colors (rand-int (count possible-colors)))]
          (q/push-matrix)
          (q/translate dx dy)
          (graded-triangle s c)
          (q/pop-matrix)
          (q/translate (/ w cols) 0)
          )
        )
        (q/pop-matrix)
        (q/translate 0 (/ h rows))
      )
    )
  (q/save "doppeldecker.png")
  )

(q/defsketch doppeldecker
  :title "doppeldecker"
  :setup setup
  :renderer :p3d
  :draw draw
  :size [1920 1080])
