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

(defn digit [r {absolute-coords :absolute-coords
                sphere-coords :sphere-coords}]
    (q/push-matrix)
    (apply q/translate (map #(* 2 r %) absolute-coords))
    (doseq [[x y z h] sphere-coords]
      (q/push-matrix)
      (apply q/translate (map #(* 2 r %) [x y z]))
      (q/fill h 255 255)
      (q/sphere r)
      (q/pop-matrix))
    (q/pop-matrix))


(defn draw []
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        r   50
        camera-x (* 2000 (q/sin (q/radians fc)))
        camera-z (* 2000 (q/cos (q/radians fc)))
        two {:absolute-coords [-8 -3 0]
             :sphere-coords   [[1 0 0 0]
                               [2 0 0 0]
                               [0 1 0 10]
                               [3 1 0 10]
                               [3 2 0 20]
                               [2 3 0 30]
                               [1 4 0 40]
                               [0 5 0 50]
                               [1 5 0 50]
                               [2 5 0 50]
                               [3 5 0 50]]}
        zero {:absolute-coords [-3 -3 0]
             :sphere-coords   [[1 0 0 0]
                               [2 0 0 0]
                               [0 1 0 10]
                               [3 1 0 10]
                               [0 2 0 20]
                               [3 2 0 20]
                               [0 3 0 30]
                               [3 3 0 30]
                               [0 4 0 40]
                               [3 4 0 40]
                               [1 5 0 50]
                               [2 5 0 50]]}
        one {:absolute-coords [2 -3 0]
             :sphere-coords   [[2 0 0 0]
                               [1 1 0 10]
                               [2 1 0 10]
                               [2 2 0 20]
                               [2 3 0 30]
                               [2 4 0 40]
                               [1 5 0 50]
                               [2 5 0 50]
                               [3 5 0 50]]}
        five {:absolute-coords [7 -3 0]
             :sphere-coords   [[0 0 0 0]
                               [1 0 0 0]
                               [2 0 0 0]
                               [3 0 0 0]
                               [0 1 0 10]
                               [0 2 0 20]
                               [1 2 0 20]
                               [2 2 0 20]
                               [3 3 0 30]
                               [0 4 0 40]
                               [3 4 0 40]
                               [1 5 0 50]
                               [2 5 0 50]]}]
    (q/background 0)
    (q/camera camera-x 0 camera-z
              0 0 0
              0 1 0)
    (doseq [n [two zero one five]]
      (digit r n))))

(q/defsketch twenty-fifteen
  :title "2015"
  :setup setup
  :draw draw
  :renderer :p3d
  :size [800 800])
