(ns fun-with-quil.animations.xmas-tree
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-lights [n max-x max-y cs]
  (into []
    (for [i (range n)]
      {:x (q/random max-x)
       :y (q/random max-y)
       :c (cs (rand-int (count cs)))
       :dθ (q/random 180)})))

(defn setup []
  (let [w  (q/width)
        h  (q/height)
        cs [[255 0 0]
            [255 255 0]
            [255 127 0]
            [0 255 0]
            [0 255 255]
            [0 0 255]
            [255 0 255]
            [127 0 255]]]
    (q/no-stroke)
    {:lights    (make-lights 50 w h cs)
     :xmas-tree (q/load-image "resources/xmas-tree.jpg")}))

(defn draw-light [{x :x y :y c :c dθ :dθ}]
  (let [fc (q/frame-count)
        light-r (q/map-range (q/sin (* 5 (q/radians (+ fc dθ)))) -1 1 15 40)]
    (q/push-matrix)
    (q/translate x y)
    (q/fill 255 255 255)
    (q/ellipse 0 0 light-r light-r)
    (apply q/fill (conj c 80))
    (q/ellipse 0 0 70 70)
    (q/pop-matrix)
    ))

(defn draw [{lights    :lights
             xmas-tree :xmas-tree}]
  (let [w  (q/width)
        h  (q/height)]
    (q/image xmas-tree 0 0)
    (q/fill 0 0 0 5)
    (q/rect 0 0 w h)
    (doseq [light lights]
      (draw-light light))
    ))

(q/defsketch xmas-tree
  :size       [1440 800]
  :title      "happy holidays"
  :setup      setup
  :draw       draw
  :middleware [m/fun-mode])
