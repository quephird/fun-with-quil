(ns fun-with-quil.animations.circle-race
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (q/smooth)
  (q/color-mode :hsb))

(defn draw []
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        ring-count 10]
    (q/background 0)
    (q/translate (* 0.5 w) (* 0.5 h))

    (dotimes [i ring-count]
      (let [n (* 3 (inc i))
            dθ (/ q/TWO-PI n)
            dφ (q/radians (* 0.05 fc (- ring-count i)(- ring-count i)))
            circle-r (* n 3)
            ring-r (+ (* 0.5 circle-r i) (* 0.5 circle-r) 5)
            h (q/map-range i 0 (dec ring-count) 50 0)
            ]
        (q/push-matrix)
        (q/rotate dφ)
        (q/fill h 255 255)
        (dotimes [_ n]
          (q/ellipse 0 ring-r circle-r circle-r)
          (q/rotate dθ)
          )
        (q/pop-matrix)))))

(q/defsketch circle-race
  :title "circle race"
  :setup setup
  :draw draw
  :size [800 800])
