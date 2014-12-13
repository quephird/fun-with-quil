(ns fun-with-quil.sketches.painter
  (:use quil.core))

(def ^:constant screen-w 500)
(def ^:constant screen-h 500)

(def grid-r (atom {}))
(def grid-g (atom {}))
(def grid-b (atom {}))


; parameter to rand-int seems to make swaths wider
; 3rd parameter to + seems to control width of square of repetition
(defn r [x y]
  (let [v (@grid-r [x y])]
    (if (not (nil? v))
      v
      (do
        (let [v (if (zero? (rand-int 999))
                   (rand-int 255)
                   (r (rem (+ x (rand-int 5) 1022) 1024) (rem (+ y (rand-int 5) 1022) 1024)))]
          (swap! grid-r update-in [[x y]] (fn [k] v))
          v)))))

(defn g [x y]
  (let [v (@grid-g [x y])]
    (if (not (nil? v))
      v
      (do
        (let [v (if (zero? (rand-int 999))
                   (rand-int 255)
                   (g (rem (+ x (rand-int 5) 1022) 1024) (rem (+ y (rand-int 5) 1022) 1024)))]
          (swap! grid-g update-in [[x y]] (fn [k] v))
          v)))))

(defn b [x y]
  (let [v (@grid-b [x y])]
    (if (not (nil? v))
      v
      (do
        (let [v (if (zero? (rand-int 999))
                   (rand-int 255)
                   (b (rem (+ x (rand-int 5) 1022) 1024) (rem (+ y (rand-int 5) 1022) 1024)))]
          (swap! grid-b update-in [[x y]] (fn [k] v))
          v)))))

(defn setup []
  (smooth)
  (no-loop))

(defn draw []
  (doseq [x (range screen-w)
          y (range screen-h)]
    (let [c [(r x y) (g x y) (b x y)]]
      (apply stroke c)
      (point x y)))
    (save "painter.png"))

(sketch
  :title "painter"
  :setup setup
  :draw draw
  :size [screen-w screen-h])
