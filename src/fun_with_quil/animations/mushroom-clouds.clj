(ns fun-with-quil.animations.mushroom-clouds
  (:use quil.core))

(def screen-w 800)
(def screen-h 800)

(def phase (atom 0))

(defn strand []
  (push-matrix)
  (let [mushroom-count 20
        ellipse-colors [[224 200 110]
                        [200 127 0]
                        [200 0 0]
                        [127 0 0]
                        [83 119 122]]
        dy (/ screen-h mushroom-count 5)]
    (doseq [i (range mushroom-count)]
      (doseq [j (range (count ellipse-colors))]
        (let [a (sin (radians (+ @phase (* 2 i (/ 360 mushroom-count)))))
              h (+ 20 (* a (+ j 4)))
              w (* h (+ j 2))
              c (ellipse-colors j)]
          (apply fill c)
          (ellipse 0 0 w h)
          (translate 0 dy)))))
  (pop-matrix)
  )

(defn setup []
  (smooth)
  (ellipse-mode :center)
  (no-stroke))

(defn draw []
  (background 0)
  (translate (/ screen-w 2) 0)
  (strand)
  (swap! phase + 5))

(sketch
  :title "mushroom clouds"
  :setup setup
  :draw draw
  :size [screen-w screen-h])
