class Trajectory {
    constructor(coordinate) {
        this.coordinate = coordinate || []
        this.curentLocationXY = null // 设备当前位置xyyaw time坐标
    }

    clear() {
        this.coordinate = []
    }

    transformSportXY(sport) {
        // 兴趣点为像素坐标时
        if (Array.isArray(sport)) {
            const [x, y, orientation] = sport
            return [x, y, orientation]
        } else if (typeof sport === "object") {
            const {x, y, orientation} = sport
            return [x, y, orientation]
        }
        return []
    }
    // 地图轨迹线段记录
    addCoordinate(sport) {
        // sport为空但list不为空时将list赋值coordinate
        if (!sport || !sport.length) {
            return
        }
        if (this.coordinate.length) {
            const lastIndex = this.coordinate.length - 1
            this.coordinate = this.coordinate.concat([[this.coordinate[lastIndex][1], this.transformSportXY(sport)]])
        } else {
            this.coordinate = this.coordinate.concat([[this.transformSportXY(sport), this.transformSportXY(sport)]])
        }
        // 每次更新设备轨迹时记录当前点
        this.curentLocationXY = sport
    }
}

export default Trajectory