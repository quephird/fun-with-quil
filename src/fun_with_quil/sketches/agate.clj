(ns fun-with-quil.sketches.agate
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-points []
  (let [n (int (q/random 8 10))]
    (for [i (range n)]
      [(+ (q/random -50 50) (* 400 (q/cos (* i (/ q/TWO-PI n)))))
       (+ (q/random -50 50) (* 300 (q/sin (* i (/ q/TWO-PI n)))))])))

(defn setup []
  (q/smooth)
  (q/no-loop)
  (q/no-fill)
  (q/color-mode :hsb)
  (make-points)
  )

(defn draw [points]
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        n  50
        bs  (into [] (repeatedly n (fn [] (q/random 255))))
        points' (concat (rest points) [(first points)])
        pairs   (map vector points points')]
    (q/background 0)
    (q/translate (* 0.5 w) (* 0.5 h))
    (q/rotate (q/radians (q/random -20 20)))
    (q/stroke-weight 6)
    (q/stroke 0 255 0)

    (doseq [i (range (dec n) 0 -1)]
      (doseq [[[x1 y1] [x2 y2]] pairs]
        (let [dx  (- x2 x1)
              dy  (- y2 y1)
              l   (Math/sqrt (+ (*  dx dx) (* dy dy)))
              r   (* 0.7 l)
              d   (Math/sqrt (- (* r r) (* 0.25 l l)))
              xm  (+ x1 (* 0.5 dx))
              ym  (+ y1 (* 0.5 dy))
              θ'  (if (< dx 0)
                    (q/acos (/ dy l))
                    (+ q/PI (q/acos (/ (- dy) l))))
              dx' (* d (q/cos θ'))
              dy' (* d (q/sin θ'))
              xc  (- xm dx')
              yc  (- ym dy')
              ϕ   (q/atan (/ l d 2))
              d' (+ (* i 10) (* 2 r))]
          (q/push-matrix)
          (q/translate xc yc)
          (q/rotate θ')
          (q/stroke 180 255 (bs i))
          (q/arc 0 0 d' d' (- ϕ) ϕ)
          (q/pop-matrix)
          ))))
    (q/save "agate.png"))

(q/defsketch agate
  :title "agate"
  :setup setup
  :draw draw
  :size [1920 1080]
  :middleware [m/fun-mode])
