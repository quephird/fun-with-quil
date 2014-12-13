(ns fun-with-quil.animations.tennis-ball
  (:use quil.core))

(def screen-w 800)
(def screen-h 800)

(defn seam [θ w]
  [(* 300 (sin (radians θ)) (cos (* w (cos (radians θ)))))
   (* 300 (sin (radians θ)) (sin (* w (cos (radians θ)))))
   (* 300 (cos (radians θ)))])

(defn setup []
  (smooth)
  (no-fill)
  (color-mode :hsb))

(defn draw []
  ; This is to create the streaking effect
  (no-stroke)
  (fill 0 255 0 5)
  (rect 0 0 screen-w screen-h)

  (let [camera-θ       (radians (* (frame-count) 0.5))
        point-count    75
        dθ             (/ 360 point-count)
        twist-factor   4]
    (translate (/ screen-w 2) (/ screen-h 2))
    (stroke-weight 15)
    (rotate-y camera-θ)
    (rotate-x camera-θ)
    (rotate-z camera-θ)
    (doseq [θ (range 0 360 dθ)]
      (let [p (seam θ twist-factor)
            h (map-range (sin (* 5 camera-θ)) -1 1 180 255)]
        (stroke h 255 255)
        (apply point p)))))

(sketch
  :title "tennis ball"
  :setup setup
  :draw draw
  :renderer :p3d
  :size [screen-w screen-h])
