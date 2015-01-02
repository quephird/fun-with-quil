(ns fun-with-quil.animations.twenty-fifteen
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (let [w (q/width)
        h (q/height)]
    (q/smooth)
    (q/background 0)
    (q/no-stroke)
    (q/color-mode :hsb)
    (q/translate (* 0.5 w) (* 0.5 h))))

(defn draw []
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        r   50
        camera-x (* 2000 (q/sin (q/radians fc)))
        camera-z (* 2000 (q/cos (q/radians fc)))
        two [[0 1 1 0]
             [1 0 0 1]
             [0 0 0 1]
             [0 0 1 0]
             [0 1 0 0]
             [1 1 1 1]]
        zero [[0 1 1 0]
              [1 0 0 1]
              [1 0 0 1]
              [1 0 0 1]
              [1 0 0 1]
              [0 1 1 0]]
        one [[0 1 0]
             [1 1 0]
             [0 1 0]
             [0 1 0]
             [0 1 0]
             [1 1 1]]
        five [[1 1 1 1]
              [1 0 0 0]
              [1 1 1 0]
              [0 0 0 1]
              [1 0 0 1]
              [0 1 1 0]]
        twenty-fifteen  (for [i (range 6)]
                          (->> [two zero one five]
                            (map #(nth % i))
                            (interpose [0])
                            (apply concat)))]
    (q/background 0)
    (q/point-light 0 0 255
                   0 -1000 camera-z)
    (q/camera camera-x 0 camera-z
              0 0 0
              0 1 0)

    (q/push-matrix)
    (q/translate (* -17 r) (* -5 r) 0)
    (doseq [[y pixel-row] (map-indexed vector twenty-fifteen)]
      (q/push-matrix)
      (doseq [[x pixel] (map-indexed vector pixel-row)]
        (if (= pixel 1)
          (let [h (q/map-range (mod (+ x y (* 0.5 fc)) 24) 0 24 0 255)]
            (q/fill h 255 255)
            (q/sphere r)))
        (q/translate (* 2 r) 0 0))
      (q/pop-matrix)
      (q/translate 0 (* 2 r) 0))
    (q/pop-matrix)))

(q/defsketch twenty-fifteen
  :title "HAPPY NEW YEAR 2015!!!"
  :setup setup
  :draw draw
  :renderer :p3d
  :size [800 800])
