(ns fun-with-quil.sketches.julia-set
  (:require [quil.core :as q :include-macros true]))

(defn norm [[^float x ^float y]]
  (Math/sqrt (+ (* x x) (* y y))))

(defn julia-fn [z [^float a ^float b] max-n]
  (loop [[^float x ^float y] z
         n 0]
    (cond
      (< 2.0 (norm [x y]))
        n
      (= n max-n)
        n
      :else
        (recur [(+ (* x x) (* -1.0 y y) a) (+ (* 2.0 x y) b)] (inc n)))))

(defn setup []
  (q/smooth)
  (q/background 0)
  (q/color-mode :hsb)
  (q/stroke-weight 1)
  (q/no-loop))

(defn draw []
  (let [w     (q/width)
        h     (q/height)
        max-x 0.5
        max-y (/ (* max-x h) w)]
    (doseq [plot-x (range w) plot-y (range h)]
      (let [x       (q/map-range plot-x 0 w -0.2 0.3)
            y       (q/map-range plot-y 0 h 0.1 0.4)
            max-n   255
            c       [-0.8 0.156]
            n       (julia-fn [x y] c max-n)
            b       (q/map-range n 0 max-n 0 255)]
      (q/stroke b 255 (- 255 b))
      (q/point plot-x plot-y)))
    (q/save "julia2.png")))

(q/defsketch julia-set
  :title "julia set"
  :setup setup
  :draw draw
  :size [1600 1000])
