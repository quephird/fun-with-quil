(ns fun-with-quil.animations.cube
  (:use quil.core))

(def screen-w 800)
(def screen-h 800)

(defn setup []
  (smooth)
  (stroke 255)
  (stroke-weight 3)
  (hint :disable-depth-test)
  (ortho))

(defn draw []
  (translate (* screen-w 0.5) (* screen-h 0.5))
  (rotate-y (/ PI 4))
  (rotate-x (radians (frame-count)))

  (let [vertices [[-100 -100 -100]
                  [-100 -100 100]
                  [100 -100 100]
                  [100 -100 -100]
                  [-100 100 -100]
                  [-100 100 100]
                  [100 100 100]
                  [100 100 -100]]
        squares [[0 1 2 3]
                 [4 5 6 7]
                 [0 1 5 4]
                 [3 2 6 7]
                 [1 2 6 5]
                 [0 3 7 4]]
        triangles [[0 7 2]
                   [0 7 5]
                   [0 5 2]
                   [2 5 7]
                   [4 3 1]
                   [4 3 6]
                   [4 6 1]
                   [1 3 6]]
        camera-θ (radians (* (frame-count) 0.5))]
    (background 0)
    (stroke 255)
    (doseq [square squares]
      (begin-shape :quads)
      (fill 255 0 0 20)
      (doseq [idx square]
        (apply vertex (vertices idx)))
      (end-shape))
    (stroke 127)
    (doseq [triangle triangles]
      (begin-shape :triangles)
      (fill 0 0 255 25)
      (doseq [idx triangle]
        (apply vertex (vertices idx)))
      (end-shape))))

(sketch
  :title "cube"
  :setup setup
  :draw draw
  :renderer :p3d
  :size [screen-w screen-h])
