(ns fun-with-quil.sketches.sun-flare-thingy
  (:use [quil.core :as q])
  (:import [processing.core PConstants]))

(defn generate-background [xs-and-cs h]
  (doseq [[[x1 c1] [x2 c2]] (map vector xs-and-cs (rest xs-and-cs))]
    (q/begin-shape :quads)
    (apply q/fill c1)
    (q/vertex x1 0)
    (q/vertex x1 h)
    (apply q/fill c2)
    (q/vertex x2 h)
    (q/vertex x2 0)
    (q/end-shape :close)))

(defn generate-starbursts [w h]
  (dotimes [_ 150]
    (let [x  (q/random w)
          y  (+ (q/random (* h 0.75)) (* h 0.12))
          d  (q/random (* 0.1 h))
          gc (q/create-graphics (* 0.25 h) (* 0.25 h))]
      (doto gc
        (.background 0 0 0 0)
        (.beginDraw)
        (.noStroke)
        (.fill 255 255 0)
        (.ellipse 100 100 (* d 1.25) (* d 1.25))
        (.filter PConstants/BLUR 15)
        (.fill 255 255 255)
        (.ellipse 100 100 (* d 0.5) (* d 0.5))
        (.filter PConstants/BLUR 4)
        (.endDraw))
      (q/image gc (- x 100) (- y 100)))))

(defn setup []
  (q/no-stroke)
  (q/no-loop))

(defn draw []
  (let [w (q/width)
        h (q/height)
        ; The calls to Math/floor below are to insure that there are no gaps
        ; between regions.
        xs-and-cs [[0 [0 0 0]]
                   [(Math/floor (* w 0.33)) [64 0 0]]
                   [(Math/floor (* w 0.5)) [200 0 0]]
                   [(Math/floor (* w 0.67)) [255 127 0]]
                   [(Math/floor (* w 0.8)) [255 255 0]]
                   [w [250 247 212]]]]
    (generate-background xs-and-cs h)
    (generate-starbursts w h)
    (q/save "sun-flare-thingy.png")))

(q/defsketch sun-flare-thingy
  :title "sun-flare-thingy"
  :setup setup
  :draw draw
  :renderer :p2d
  :size [1440 800])
