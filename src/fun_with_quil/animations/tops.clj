(ns fun-with-quil.animations.tops
  (:use quil.core))

(def screen [1920 1080])
(def all-tops (atom {}))

(defn rand-hues [n]
  (into [] (repeatedly n (fn [] (rand-int 256)))))

(defn make-top [[screen-x screen-y] hues]
  {:x (random screen-x)
   :y (random screen-y)
   :r (random 25 75)
   :dx (random -3 3)
   :dy (random -3 3)
   :θ (random 360)
   :h (hues (rand-int (count hues)))})

(defn init-tops [n screen hues]
  (reset! all-tops
    (into {}
      (for [i (range n)]
        [i (make-top screen hues)]))))

(defn update-top [tops i screen]
  (let [{x :x y :y dx :dx dy :dy θ :θ} (@tops i)
        [max-x max-y] screen]
    (swap! tops update-in [i :θ] (fn [θ] (+ θ 3)))
    ; Make tops bounce off "walls"
    (swap! tops update-in [i :dx] (fn [dx] (if (or (<= (+ x dx) 0) (>= (+ x dx) max-x)) (- dx) dx)))
    (swap! tops update-in [i :dy] (fn [dy] (if (or (<= (+ y dy) 0) (>= (+ y dy) max-y)) (- dy) dy)))
    ; Throw in some randomness in their "walk"
    (swap! tops update-in [i :x] (fn [x] (+ x dx (random -2 2))))
    (swap! tops update-in [i :y] (fn [y] (+ y dy (random -2 2))))))

(defn draw-top [{x :x y :y r :r θ :θ h :h}]
  (stroke h 255 255 5)
  (stroke-weight 2)
  (let [l 250
        t (radians θ)
        tangent-x (+ x (* r (cos t)))
        tangent-y (+ y (* r (sin t)))
        x1 (+ tangent-x (* l (sin t)))
        y1 (- tangent-y (* l (cos t)))
        x2 (- tangent-x (* l (sin t)))
        y2 (+ tangent-y (* l (cos t)))]
    (line x1 y1 x2 y2)))

(defn key-pressed []
  (save "tops.png"))

(defn setup []
  (let [top-count 5
        color-count 3]
    (smooth)
    (background 0)
    (init-tops top-count screen (rand-hues color-count))
    (color-mode :hsb)))

(defn draw []
  (doseq [[i top] @all-tops]
    (draw-top top)
    (update-top all-tops i screen)))

(sketch
  :title "tops"
  :setup setup
  :draw draw
  :key-pressed key-pressed
  :size screen)
