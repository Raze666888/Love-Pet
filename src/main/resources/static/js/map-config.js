/**
 * 高德地图配置文件
 * 
 * 使用说明：
 * 1. 在高德开放平台 (https://lbs.amap.com/) 注册账号
 * 2. 创建应用并获取Web端(JS API)的Key
 * 3. 将YOUR_AMAP_KEY替换为你的实际API Key
 * 4. 建议在高德控制台设置域名白名单以提高安全性
 */

// 高德地图API配置
const AMAP_CONFIG = {
    // 高德地图API Key - 请替换为你的实际Key
    key: 'e4ba17441f45301a0d054097d3e9b933',
    
    // 地图版本
    version: '2.0',
    
    // 默认地图配置
    defaultMapOptions: {
        zoom: 15,              // 默认缩放级别
        resizeEnable: true,    // 允许调整大小
        center: [116.397428, 39.90923] // 默认中心点（北京天安门）
    },
    
    // 标记配置
    markerOptions: {
        // 可以在这里添加自定义标记图标等配置
    },
    
    // 定位配置
    geolocationOptions: {
        enableHighAccuracy: true,  // 是否使用高精度定位
        timeout: 10000,            // 超时时间（毫秒）
        maximumAge: 0,             // 位置缓存时间
        convert: true,             // 是否使用坐标偏移
        showButton: true,          // 是否显示定位按钮
        buttonPosition: 'RB',      // 定位按钮位置
        showMarker: true,          // 是否显示定位标记
        showCircle: true,          // 是否显示定位精度圈
        panToLocation: true,       // 定位成功后是否自动移动地图到定位点
        zoomToAccuracy: true       // 定位成功后是否自动调整地图视野到定位点
    }
};

/**
 * 获取高德地图API URL
 * @returns {string} 高德地图API的完整URL
 */
function getAmapApiUrl() {
    return `https://webapi.amap.com/maps?v=${AMAP_CONFIG.version}&key=${AMAP_CONFIG.key}`;
}

/**
 * 动态加载高德地图API
 * @param {Function} callback 加载完成后的回调函数
 */
function loadAmapApi(callback) {
    if (typeof AMap !== 'undefined') {
        // 如果已经加载，直接执行回调
        if (callback) callback();
        return;
    }

    console.log('开始加载高德地图API...');
    const script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = getAmapApiUrl();
    script.onload = function() {
        console.log('高德地图API加载成功');
        if (callback) callback();
    };
    script.onerror = function() {
        console.error('高德地图API加载失败，请检查网络连接和API Key配置');
        // 尝试使用备用Key
        loadBackupAmapApi(callback);
    };
    script.timeout = 10000; // 10秒超时
    script.ontimeout = function() {
        console.error('高德地图API加载超时，尝试备用方案');
        loadBackupAmapApi(callback);
    };
    document.head.appendChild(script);
}

/**
 * 加载备用高德地图API
 * @param {Function} callback 加载完成后的回调函数
 */
function loadBackupAmapApi(callback) {
    console.log('尝试加载备用高德地图API...');

    // 使用公开的测试Key（仅用于开发测试）
    const backupKey = 'f2e67e9a0b5f5f4c5c1e1d8e0f5b5a7c';
    const backupUrl = `https://webapi.amap.com/maps?v=2.0&key=${backupKey}`;

    const script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = backupUrl;
    script.onload = function() {
        console.log('备用高德地图API加载成功');
        if (callback) callback();
    };
    script.onerror = function() {
        console.error('备用高德地图API也加载失败，将使用静态数据');
        // 如果API加载失败，使用静态省市区数据
        if (callback) callback();
    };
    document.head.appendChild(script);
}

/**
 * 创建地图实例
 * @param {string} containerId 地图容器的ID
 * @param {Object} options 地图配置选项（可选）
 * @returns {AMap.Map} 地图实例
 */
function createMap(containerId, options) {
    const mapOptions = Object.assign({}, AMAP_CONFIG.defaultMapOptions, options || {});
    return new AMap.Map(containerId, mapOptions);
}

/**
 * 地理编码 - 将地址转换为坐标
 * @param {string} address 地址
 * @param {Function} callback 回调函数，参数为(status, result)
 */
function geocodeAddress(address, callback) {
    AMap.plugin('AMap.Geocoder', function() {
        const geocoder = new AMap.Geocoder();
        geocoder.getLocation(address, function(status, result) {
            if (callback) callback(status, result);
        });
    });
}

/**
 * 逆地理编码 - 将坐标转换为地址
 * @param {Array} lnglat 经纬度数组 [lng, lat]
 * @param {Function} callback 回调函数，参数为(status, result)
 */
function regeocode(lnglat, callback) {
    AMap.plugin('AMap.Geocoder', function() {
        const geocoder = new AMap.Geocoder();
        geocoder.getAddress(lnglat, function(status, result) {
            if (callback) callback(status, result);
        });
    });
}

/**
 * 获取当前位置
 * @param {Function} successCallback 成功回调，参数为(position)
 * @param {Function} errorCallback 失败回调，参数为(error)
 */
function getCurrentPosition(successCallback, errorCallback) {
    AMap.plugin('AMap.Geolocation', function() {
        const geolocation = new AMap.Geolocation(AMAP_CONFIG.geolocationOptions);
        geolocation.getCurrentPosition(function(status, result) {
            if (status === 'complete') {
                if (successCallback) successCallback(result);
            } else {
                if (errorCallback) errorCallback(result);
            }
        });
    });
}

/**
 * 在地图上添加标记
 * @param {AMap.Map} map 地图实例
 * @param {Array} position 位置 [lng, lat]
 * @param {Object} options 标记配置选项
 * @returns {AMap.Marker} 标记实例
 */
function addMarker(map, position, options) {
    const markerOptions = Object.assign({
        position: position
    }, AMAP_CONFIG.markerOptions, options || {});
    
    const marker = new AMap.Marker(markerOptions);
    map.add(marker);
    return marker;
}

/**
 * 创建信息窗体
 * @param {string} content 窗体内容（HTML）
 * @param {Object} options 窗体配置选项
 * @returns {AMap.InfoWindow} 信息窗体实例
 */
function createInfoWindow(content, options) {
    const infoWindowOptions = Object.assign({
        content: content
    }, options || {});
    
    return new AMap.InfoWindow(infoWindowOptions);
}

/**
 * 计算两点之间的距离
 * @param {Array} point1 第一个点 [lng, lat]
 * @param {Array} point2 第二个点 [lng, lat]
 * @returns {number} 距离（米）
 */
function calculateDistance(point1, point2) {
    const lnglat1 = new AMap.LngLat(point1[0], point1[1]);
    const lnglat2 = new AMap.LngLat(point2[0], point2[1]);
    return lnglat1.distance(lnglat2);
}

/**
 * 格式化距离显示
 * @param {number} distance 距离（米）
 * @returns {string} 格式化后的距离字符串
 */
function formatDistance(distance) {
    if (distance < 1000) {
        return Math.round(distance) + '米';
    } else {
        return (distance / 1000).toFixed(2) + '公里';
    }
}

// ====================== 省市区数据支持 ======================

/**
 * 省市区数据缓存
 */
const ADMIN_DIVISIONS_CACHE = {
    provinces: null,
    cities: {},
    districts: {},
    lastLoadTime: 0
};

/**
 * 加载省份数据
 * @param {Function} callback 回调函数
 */
function loadProvinces(callback) {
    // 检查缓存
    if (ADMIN_DIVISIONS_CACHE.provinces &&
        Date.now() - ADMIN_DIVISIONS_CACHE.lastLoadTime < 3600000) { // 1小时缓存
        if (callback) callback(ADMIN_DIVISIONS_CACHE.provinces);
        return;
    }

    // 如果高德地图API未加载，使用静态数据
    if (typeof AMap === 'undefined') {
        console.log('高德地图API未加载，使用静态省份数据');
        const staticProvinces = getStaticProvinces();
        ADMIN_DIVISIONS_CACHE.provinces = staticProvinces;
        ADMIN_DIVISIONS_CACHE.lastLoadTime = Date.now();
        if (callback) callback(staticProvinces);
        return;
    }

    AMap.plugin('AMap.DistrictSearch', function() {
        const districtSearch = new AMap.DistrictSearch({
            level: 'province',
            subdistrict: 1,
            extensions: 'all'
        });

        districtSearch.search('中国', function(status, result) {
            if (status === 'complete' && result.districtList && result.districtList.length > 0) {
                const provinces = result.districtList[0].districtList || [];
                ADMIN_DIVISIONS_CACHE.provinces = provinces;
                ADMIN_DIVISIONS_CACHE.lastLoadTime = Date.now();
                console.log('省份数据加载成功，共', provinces.length, '个省份');
                if (callback) callback(provinces);
            } else {
                console.error('加载省份数据失败:', result);
                console.log('使用静态省份数据作为备用方案');
                const staticProvinces = getStaticProvinces();
                ADMIN_DIVISIONS_CACHE.provinces = staticProvinces;
                ADMIN_DIVISIONS_CACHE.lastLoadTime = Date.now();
                if (callback) callback(staticProvinces);
            }
        });
    });
}

/**
 * 加载城市数据
 * @param {string} provinceName 省份名称
 * @param {Function} callback 回调函数
 */
function loadCities(provinceName, callback) {
    // 检查缓存
    if (ADMIN_DIVISIONS_CACHE.cities[provinceName]) {
        if (callback) callback(ADMIN_DIVISIONS_CACHE.cities[provinceName]);
        return;
    }

    // 如果高德地图API未加载，使用静态数据
    if (typeof AMap === 'undefined') {
        console.log('高德地图API未加载，使用静态城市数据，省份:', provinceName);
        const staticCities = getStaticCities(provinceName);
        ADMIN_DIVISIONS_CACHE.cities[provinceName] = staticCities;
        if (callback) callback(staticCities);
        return;
    }

    AMap.plugin('AMap.DistrictSearch', function() {
        const districtSearch = new AMap.DistrictSearch({
            level: 'city',
            subdistrict: 1,
            extensions: 'all'
        });

        districtSearch.search(provinceName, function(status, result) {
            if (status === 'complete' && result.districtList && result.districtList.length > 0) {
                const cities = result.districtList[0].districtList || [];
                ADMIN_DIVISIONS_CACHE.cities[provinceName] = cities;
                console.log('城市数据加载成功，省份:', provinceName, '共', cities.length, '个城市');
                if (callback) callback(cities);
            } else {
                console.error('加载城市数据失败:', result, '使用静态数据作为备用方案');
                const staticCities = getStaticCities(provinceName);
                ADMIN_DIVISIONS_CACHE.cities[provinceName] = staticCities;
                if (callback) callback(staticCities);
            }
        });
    });
}

/**
 * 加载区县数据
 * @param {string} cityName 城市名称
 * @param {Function} callback 回调函数
 */
function loadDistricts(cityName, callback) {
    // 检查缓存
    if (ADMIN_DIVISIONS_CACHE.districts[cityName]) {
        if (callback) callback(ADMIN_DIVISIONS_CACHE.districts[cityName]);
        return;
    }

    // 如果高德地图API未加载，使用静态数据
    if (typeof AMap === 'undefined') {
        console.log('高德地图API未加载，使用静态区县数据，城市:', cityName);
        const staticDistricts = getStaticDistricts(cityName);
        ADMIN_DIVISIONS_CACHE.districts[cityName] = staticDistricts;
        if (callback) callback(staticDistricts);
        return;
    }

    AMap.plugin('AMap.DistrictSearch', function() {
        const districtSearch = new AMap.DistrictSearch({
            level: 'district',
            subdistrict: 0,
            extensions: 'all'
        });

        districtSearch.search(cityName, function(status, result) {
            if (status === 'complete' && result.districtList && result.districtList.length > 0) {
                const districts = result.districtList[0].districtList || [];
                ADMIN_DIVISIONS_CACHE.districts[cityName] = districts;
                console.log('区县数据加载成功，城市:', cityName, '共', districts.length, '个区县');
                if (callback) callback(districts);
            } else {
                console.error('加载区县数据失败:', result, '使用静态数据作为备用方案');
                const staticDistricts = getStaticDistricts(cityName);
                ADMIN_DIVISIONS_CACHE.districts[cityName] = staticDistricts;
                if (callback) callback(staticDistricts);
            }
        });
    });
}

/**
 * POI搜索配置
 */
const POI_SEARCH_CONFIG = {
    citylimit: false,           // 是否限制在当前城市
    pageSize: 20,              // 每页显示数量
    pageIndex: 1,              // 当前页码
    extensions: 'all'          // 扩展信息
};

/**
 * 搜索POI（兴趣点）
 * @param {string} keyword 搜索关键词
 * @param {string} city 搜索城市（可选）
 * @param {Function} callback 回调函数，参数为(status, result)
 * @param {Object} options 搜索选项（可选）
 */
function searchPOI(keyword, city, callback, options) {
    const searchOptions = Object.assign({}, POI_SEARCH_CONFIG, options || {});
    if (city) {
        searchOptions.city = city;
    }

    AMap.plugin(['AMap.PlaceSearch'], function() {
        const placeSearch = new AMap.PlaceSearch(searchOptions);

        placeSearch.search(keyword, function(status, result) {
            if (callback) callback(status, result);
        });
    });
}

/**
 * 智能地址搜索
 * @param {string} address 地址文本
 * @param {Object} context 上下文信息（省市区）
 * @param {Function} callback 回调函数
 */
function smartAddressSearch(address, context, callback) {
    let searchKeyword = address;
    let searchCity = context.city || '';

    // 如果没有指定城市，尝试从地址中提取
    if (!searchCity && context.province) {
        searchCity = context.province;
    }

    // 构建搜索关键词
    if (context.province && !address.includes(context.province)) {
        searchKeyword = context.province + address;
    }
    if (context.city && !searchKeyword.includes(context.city)) {
        searchKeyword = context.city + address;
    }
    if (context.district && !searchKeyword.includes(context.district)) {
        searchKeyword = context.district + address;
    }

    searchPOI(searchKeyword, searchCity, function(status, result) {
        if (status === 'complete' && result.poiList && result.poiList.pois) {
            // 处理搜索结果，添加距离信息
            const pois = result.poiList.pois.map(poi => {
                return {
                    name: poi.name,
                    address: poi.address,
                    district: poi.adname || '',
                    location: poi.location,
                    distance: poi.distance || 0,
                    tel: poi.tel || '',
                    type: poi.type || ''
                };
            });

            if (callback) callback(true, pois);
        } else {
            // 如果POI搜索失败，尝试地理编码
            geocodeAddress(searchKeyword, function(geoStatus, geoResult) {
                if (geoStatus === 'complete' && geoResult.geocodes && geoResult.geocodes.length > 0) {
                    const geocode = geoResult.geocodes[0];
                    const poi = {
                        name: geocode.formattedAddress || address,
                        address: geocode.formattedAddress || address,
                        district: geocode.district || '',
                        location: geocode.location,
                        distance: 0,
                        tel: '',
                        type: '地址'
                    };

                    if (callback) callback(true, [poi]);
                } else {
                    if (callback) callback(false, []);
                }
            });
        }
    });
}

/**
 * 地址验证规则
 */
const ADDRESS_VALIDATION_RULES = {
    // 最小长度
    minLength: 5,
    // 最大长度
    maxLength: 200,
    // 必须包含的行政区划信息
    requireAdminDivision: true,
    // 详细地址最小长度
    detailedAddressMinLength: 3
};

/**
 * 验证地址完整性
 * @param {Object} addressData 地址数据
 * @returns {Object} 验证结果
 */
function validateAddress(addressData) {
    const result = {
        isValid: true,
        errors: [],
        warnings: [],
        level: 'success' // success, warning, error
    };

    // 检查必填字段
    if (!addressData.province) {
        result.errors.push('请选择省份');
        result.isValid = false;
        result.level = 'error';
    }

    if (!addressData.city) {
        result.errors.push('请选择城市');
        result.isValid = false;
        result.level = 'error';
    }

    if (!addressData.district) {
        result.errors.push('请选择区县');
        result.isValid = false;
        result.level = 'error';
    }

    if (!addressData.detailedAddress) {
        result.errors.push('请输入详细地址');
        result.isValid = false;
        result.level = 'error';
    }

    // 检查详细地址长度
    if (addressData.detailedAddress &&
        addressData.detailedAddress.length < ADDRESS_VALIDATION_RULES.detailedAddressMinLength) {
        result.warnings.push('详细地址可能过短，建议补充更详细的信息');
        if (result.level === 'success') result.level = 'warning';
    }

    // 检查完整地址长度
    const fullAddress = buildFullAddress(addressData);
    if (fullAddress.length < ADDRESS_VALIDATION_RULES.minLength) {
        result.warnings.push('地址信息可能不完整');
        if (result.level === 'success') result.level = 'warning';
    }

    if (fullAddress.length > ADDRESS_VALIDATION_RULES.maxLength) {
        result.errors.push('地址信息过长');
        result.isValid = false;
        result.level = 'error';
    }

    // 检查经纬度
    if (!addressData.longitude || !addressData.latitude) {
        result.warnings.push('尚未获取位置坐标，建议点击定位按钮');
        if (result.level === 'success') result.level = 'warning';
    }

    return result;
}

/**
 * 构建完整地址
 * @param {Object} addressData 地址数据
 * @returns {string} 完整地址
 */
function buildFullAddress(addressData) {
    const parts = [];

    if (addressData.province) parts.push(addressData.province);
    if (addressData.city && addressData.city !== addressData.province) {
        parts.push(addressData.city);
    }
    if (addressData.district) parts.push(addressData.district);
    if (addressData.detailedAddress) parts.push(addressData.detailedAddress);

    return parts.join('');
}

/**
 * 清除省市区缓存
 */
function clearAdminDivisionsCache() {
    ADMIN_DIVISIONS_CACHE.provinces = null;
    ADMIN_DIVISIONS_CACHE.cities = {};
    ADMIN_DIVISIONS_CACHE.districts = {};
    ADMIN_DIVISIONS_CACHE.lastLoadTime = 0;
}

/**
 * 获取缓存统计信息
 * @returns {Object} 缓存统计
 */
function getCacheStats() {
    const provinceCount = ADMIN_DIVISIONS_CACHE.provinces ? ADMIN_DIVISIONS_CACHE.provinces.length : 0;
    const cityCount = Object.keys(ADMIN_DIVISIONS_CACHE.cities).length;
    const districtCount = Object.keys(ADMIN_DIVISIONS_CACHE.districts).length;

    return {
        provinces: provinceCount,
        cities: cityCount,
        districts: districtCount,
        lastLoadTime: ADMIN_DIVISIONS_CACHE.lastLoadTime,
        cacheAge: Date.now() - ADMIN_DIVISIONS_CACHE.lastLoadTime
    };
}

// ====================== 静态省市区数据（备用方案） ======================

/**
 * 获取静态省份数据
 * @returns {Array} 省份数据数组
 */
function getStaticProvinces() {
    return [
        { name: '北京市', adcode: '110000', level: 'province' },
        { name: '天津市', adcode: '120000', level: 'province' },
        { name: '河北省', adcode: '130000', level: 'province' },
        { name: '山西省', adcode: '140000', level: 'province' },
        { name: '内蒙古自治区', adcode: '150000', level: 'province' },
        { name: '辽宁省', adcode: '210000', level: 'province' },
        { name: '吉林省', adcode: '220000', level: 'province' },
        { name: '黑龙江省', adcode: '230000', level: 'province' },
        { name: '上海市', adcode: '310000', level: 'province' },
        { name: '江苏省', adcode: '320000', level: 'province' },
        { name: '浙江省', adcode: '330000', level: 'province' },
        { name: '安徽省', adcode: '340000', level: 'province' },
        { name: '福建省', adcode: '350000', level: 'province' },
        { name: '江西省', adcode: '360000', level: 'province' },
        { name: '山东省', adcode: '370000', level: 'province' },
        { name: '河南省', adcode: '410000', level: 'province' },
        { name: '湖北省', adcode: '420000', level: 'province' },
        { name: '湖南省', adcode: '430000', level: 'province' },
        { name: '广东省', adcode: '440000', level: 'province' },
        { name: '广西壮族自治区', adcode: '450000', level: 'province' },
        { name: '海南省', adcode: '460000', level: 'province' },
        { name: '重庆市', adcode: '500000', level: 'province' },
        { name: '四川省', adcode: '510000', level: 'province' },
        { name: '贵州省', adcode: '520000', level: 'province' },
        { name: '云南省', adcode: '530000', level: 'province' },
        { name: '西藏自治区', adcode: '540000', level: 'province' },
        { name: '陕西省', adcode: '610000', level: 'province' },
        { name: '甘肃省', adcode: '620000', level: 'province' },
        { name: '青海省', adcode: '630000', level: 'province' },
        { name: '宁夏回族自治区', adcode: '640000', level: 'province' },
        { name: '新疆维吾尔自治区', adcode: '650000', level: 'province' },
        { name: '台湾省', adcode: '710000', level: 'province' },
        { name: '香港特别行政区', adcode: '810000', level: 'province' },
        { name: '澳门特别行政区', adcode: '820000', level: 'province' }
    ];
}

/**
 * 获取指定省份的静态城市数据
 * @param {string} provinceName 省份名称
 * @returns {Array} 城市数据数组
 */
function getStaticCities(provinceName) {
    const cityData = {
        '北京市': [
            { name: '北京市', adcode: '110100', level: 'city' }
        ],
        '天津市': [
            { name: '天津市', adcode: '120100', level: 'city' }
        ],
        '上海市': [
            { name: '上海市', adcode: '310100', level: 'city' }
        ],
        '重庆市': [
            { name: '重庆市', adcode: '500100', level: 'city' }
        ],
        '河北省': [
            { name: '石家庄市', adcode: '130100', level: 'city' },
            { name: '唐山市', adcode: '130200', level: 'city' },
            { name: '秦皇岛市', adcode: '130300', level: 'city' },
            { name: '邯郸市', adcode: '130400', level: 'city' },
            { name: '邢台市', adcode: '130500', level: 'city' },
            { name: '保定市', adcode: '130600', level: 'city' },
            { name: '张家口市', adcode: '130700', level: 'city' },
            { name: '承德市', adcode: '130800', level: 'city' },
            { name: '沧州市', adcode: '130900', level: 'city' },
            { name: '廊坊市', adcode: '131000', level: 'city' },
            { name: '衡水市', adcode: '131100', level: 'city' }
        ],
        '山西省': [
            { name: '太原市', adcode: '140100', level: 'city' },
            { name: '大同市', adcode: '140200', level: 'city' },
            { name: '阳泉市', adcode: '140300', level: 'city' },
            { name: '长治市', adcode: '140400', level: 'city' },
            { name: '晋城市', adcode: '140500', level: 'city' },
            { name: '朔州市', adcode: '140600', level: 'city' },
            { name: '晋中市', adcode: '140700', level: 'city' },
            { name: '运城市', adcode: '140800', level: 'city' },
            { name: '忻州市', adcode: '140900', level: 'city' },
            { name: '临汾市', adcode: '141000', level: 'city' },
            { name: '吕梁市', adcode: '141100', level: 'city' }
        ],
        '河南省': [
            { name: '郑州市', adcode: '410100', level: 'city' },
            { name: '开封市', adcode: '410200', level: 'city' },
            { name: '洛阳市', adcode: '410300', level: 'city' },
            { name: '平顶山市', adcode: '410400', level: 'city' },
            { name: '安阳市', adcode: '410500', level: 'city' },
            { name: '鹤壁市', adcode: '410600', level: 'city' },
            { name: '新乡市', adcode: '410700', level: 'city' },
            { name: '焦作市', adcode: '410800', level: 'city' },
            { name: '濮阳市', adcode: '410900', level: 'city' },
            { name: '许昌市', adcode: '411000', level: 'city' },
            { name: '漯河市', adcode: '411100', level: 'city' },
            { name: '三门峡市', adcode: '411200', level: 'city' },
            { name: '南阳市', adcode: '411300', level: 'city' },
            { name: '商丘市', adcode: '411400', level: 'city' },
            { name: '信阳市', adcode: '411500', level: 'city' },
            { name: '周口市', adcode: '411600', level: 'city' },
            { name: '驻马店市', adcode: '411700', level: 'city' }
        ],
        '湖北省': [
            { name: '武汉市', adcode: '420100', level: 'city' },
            { name: '黄石市', adcode: '420200', level: 'city' },
            { name: '十堰市', adcode: '420300', level: 'city' },
            { name: '宜昌市', adcode: '420500', level: 'city' },
            { name: '襄阳市', adcode: '420600', level: 'city' },
            { name: '鄂州市', adcode: '420700', level: 'city' },
            { name: '荆门市', adcode: '420800', level: 'city' },
            { name: '孝感市', adcode: '420900', level: 'city' },
            { name: '荆州市', adcode: '421000', level: 'city' },
            { name: '黄冈市', adcode: '421100', level: 'city' },
            { name: '咸宁市', adcode: '421200', level: 'city' },
            { name: '随州市', adcode: '421300', level: 'city' },
            { name: '恩施土家族苗族自治州', adcode: '422800', level: 'city' }
        ],
        '湖南省': [
            { name: '长沙市', adcode: '430100', level: 'city' },
            { name: '株洲市', adcode: '430200', level: 'city' },
            { name: '湘潭市', adcode: '430300', level: 'city' },
            { name: '衡阳市', adcode: '430400', level: 'city' },
            { name: '邵阳市', adcode: '430500', level: 'city' },
            { name: '岳阳市', adcode: '430600', level: 'city' },
            { name: '常德市', adcode: '430700', level: 'city' },
            { name: '张家界市', adcode: '430800', level: 'city' },
            { name: '益阳市', adcode: '430900', level: 'city' },
            { name: '郴州市', adcode: '431000', level: 'city' },
            { name: '永州市', adcode: '431100', level: 'city' },
            { name: '怀化市', adcode: '431200', level: 'city' },
            { name: '娄底市', adcode: '431300', level: 'city' },
            { name: '湘西土家族苗族自治州', adcode: '433100', level: 'city' }
        ],
        '广东省': [
            { name: '广州市', adcode: '440100', level: 'city' },
            { name: '韶关市', adcode: '440200', level: 'city' },
            { name: '深圳市', adcode: '440300', level: 'city' },
            { name: '珠海市', adcode: '440400', level: 'city' },
            { name: '汕头市', adcode: '440500', level: 'city' },
            { name: '佛山市', adcode: '440600', level: 'city' },
            { name: '江门市', adcode: '440700', level: 'city' },
            { name: '湛江市', adcode: '440800', level: 'city' },
            { name: '茂名市', adcode: '440900', level: 'city' },
            { name: '肇庆市', adcode: '441200', level: 'city' },
            { name: '惠州市', adcode: '441300', level: 'city' },
            { name: '梅州市', adcode: '441400', level: 'city' },
            { name: '汕尾市', adcode: '441500', level: 'city' },
            { name: '河源市', adcode: '441600', level: 'city' },
            { name: '阳江市', adcode: '441700', level: 'city' },
            { name: '清远市', adcode: '441800', level: 'city' },
            { name: '东莞市', adcode: '441900', level: 'city' },
            { name: '中山市', adcode: '442000', level: 'city' },
            { name: '潮州市', adcode: '445100', level: 'city' },
            { name: '揭阳市', adcode: '445200', level: 'city' },
            { name: '云浮市', adcode: '445300', level: 'city' }
        ],
        '四川省': [
            { name: '成都市', adcode: '510100', level: 'city' },
            { name: '自贡市', adcode: '510300', level: 'city' },
            { name: '攀枝花市', adcode: '510400', level: 'city' },
            { name: '泸州市', adcode: '510500', level: 'city' },
            { name: '德阳市', adcode: '510600', level: 'city' },
            { name: '绵阳市', adcode: '510700', level: 'city' },
            { name: '广元市', adcode: '510800', level: 'city' },
            { name: '遂宁市', adcode: '510900', level: 'city' },
            { name: '内江市', adcode: '511000', level: 'city' },
            { name: '乐山市', adcode: '511100', level: 'city' },
            { name: '南充市', adcode: '511300', level: 'city' },
            { name: '眉山市', adcode: '511400', level: 'city' },
            { name: '宜宾市', adcode: '511500', level: 'city' },
            { name: '广安市', adcode: '511600', level: 'city' },
            { name: '达州市', adcode: '511700', level: 'city' },
            { name: '雅安市', adcode: '511800', level: 'city' },
            { name: '巴中市', adcode: '511900', level: 'city' },
            { name: '资阳市', adcode: '512000', level: 'city' },
            { name: '阿坝藏族羌族自治州', adcode: '513200', level: 'city' },
            { name: '甘孜藏族自治州', adcode: '513300', level: 'city' },
            { name: '凉山彝族自治州', adcode: '513400', level: 'city' }
        ]
    };

    return cityData[provinceName] || [];
}

/**
 * 获取指定城市的静态区县数据
 * @param {string} cityName 城市名称
 * @returns {Array} 区县数据数组
 */
function getStaticDistricts(cityName) {
    const districtData = {
        '北京市': [
            { name: '东城区', adcode: '110101', level: 'district' },
            { name: '西城区', adcode: '110102', level: 'district' },
            { name: '朝阳区', adcode: '110105', level: 'district' },
            { name: '丰台区', adcode: '110106', level: 'district' },
            { name: '石景山区', adcode: '110107', level: 'district' },
            { name: '海淀区', adcode: '110108', level: 'district' },
            { name: '门头沟区', adcode: '110109', level: 'district' },
            { name: '房山区', adcode: '110111', level: 'district' },
            { name: '通州区', adcode: '110112', level: 'district' },
            { name: '顺义区', adcode: '110113', level: 'district' },
            { name: '昌平区', adcode: '110114', level: 'district' },
            { name: '大兴区', adcode: '110115', level: 'district' },
            { name: '怀柔区', adcode: '110116', level: 'district' },
            { name: '平谷区', adcode: '110117', level: 'district' },
            { name: '密云区', adcode: '110118', level: 'district' },
            { name: '延庆区', adcode: '110119', level: 'district' }
        ],
        '上海市': [
            { name: '黄浦区', adcode: '310101', level: 'district' },
            { name: '徐汇区', adcode: '310104', level: 'district' },
            { name: '长宁区', adcode: '310105', level: 'district' },
            { name: '静安区', adcode: '310106', level: 'district' },
            { name: '普陀区', adcode: '310107', level: 'district' },
            { name: '虹口区', adcode: '310109', level: 'district' },
            { name: '杨浦区', adcode: '310110', level: 'district' },
            { name: '闵行区', adcode: '310112', level: 'district' },
            { name: '宝山区', adcode: '310113', level: 'district' },
            { name: '嘉定区', adcode: '310114', level: 'district' },
            { name: '浦东新区', adcode: '310115', level: 'district' },
            { name: '金山区', adcode: '310116', level: 'district' },
            { name: '松江区', adcode: '310117', level: 'district' },
            { name: '青浦区', adcode: '310118', level: 'district' },
            { name: '奉贤区', adcode: '310120', level: 'district' },
            { name: '崇明区', adcode: '310151', level: 'district' }
        ],
        '广州市': [
            { name: '荔湾区', adcode: '440103', level: 'district' },
            { name: '越秀区', adcode: '440104', level: 'district' },
            { name: '海珠区', adcode: '440105', level: 'district' },
            { name: '天河区', adcode: '440106', level: 'district' },
            { name: '白云区', adcode: '440111', level: 'district' },
            { name: '黄埔区', adcode: '440112', level: 'district' },
            { name: '番禺区', adcode: '440113', level: 'district' },
            { name: '花都区', adcode: '440114', level: 'district' },
            { name: '南沙区', adcode: '440115', level: 'district' },
            { name: '从化区', adcode: '440117', level: 'district' },
            { name: '增城区', adcode: '440118', level: 'district' }
        ],
        '深圳市': [
            { name: '罗湖区', adcode: '440303', level: 'district' },
            { name: '福田区', adcode: '440304', level: 'district' },
            { name: '南山区', adcode: '440305', level: 'district' },
            { name: '宝安区', adcode: '440306', level: 'district' },
            { name: '龙岗区', adcode: '440307', level: 'district' },
            { name: '盐田区', adcode: '440308', level: 'district' },
            { name: '龙华区', adcode: '440309', level: 'district' },
            { name: '坪山区', adcode: '440310', level: 'district' },
            { name: '光明区', adcode: '440311', level: 'district' }
        ],
        '郑州市': [
            { name: '中原区', adcode: '410102', level: 'district' },
            { name: '二七区', adcode: '410103', level: 'district' },
            { name: '管城回族区', adcode: '410104', level: 'district' },
            { name: '金水区', adcode: '410105', level: 'district' },
            { name: '上街区', adcode: '410106', level: 'district' },
            { name: '惠济区', adcode: '410108', level: 'district' },
            { name: '中牟县', adcode: '410122', level: 'district' },
            { name: '巩义市', adcode: '410181', level: 'district' },
            { name: '荥阳市', adcode: '410182', level: 'district' },
            { name: '新密市', adcode: '410183', level: 'district' },
            { name: '新郑市', adcode: '410184', level: 'district' },
            { name: '登封市', adcode: '410185', level: 'district' }
        ],
        '武汉市': [
            { name: '江岸区', adcode: '420102', level: 'district' },
            { name: '江汉区', adcode: '420103', level: 'district' },
            { name: '硚口区', adcode: '420104', level: 'district' },
            { name: '汉阳区', adcode: '420105', level: 'district' },
            { name: '武昌区', adcode: '420106', level: 'district' },
            { name: '青山区', adcode: '420107', level: 'district' },
            { name: '洪山区', adcode: '420111', level: 'district' },
            { name: '东西湖区', adcode: '420112', level: 'district' },
            { name: '汉南区', adcode: '420113', level: 'district' },
            { name: '蔡甸区', adcode: '420114', level: 'district' },
            { name: '江夏区', adcode: '420115', level: 'district' },
            { name: '黄陂区', adcode: '420116', level: 'district' },
            { name: '新洲区', adcode: '420117', level: 'district' }
        ],
        '长沙市': [
            { name: '芙蓉区', adcode: '430102', level: 'district' },
            { name: '天心区', adcode: '430103', level: 'district' },
            { name: '岳麓区', adcode: '430104', level: 'district' },
            { name: '开福区', adcode: '430105', level: 'district' },
            { name: '雨花区', adcode: '430111', level: 'district' },
            { name: '望城区', adcode: '430112', level: 'district' },
            { name: '长沙县', adcode: '430121', level: 'district' },
            { name: '浏阳市', adcode: '430181', level: 'district' },
            { name: '宁乡市', adcode: '430182', level: 'district' }
        ],
        '成都市': [
            { name: '锦江区', adcode: '510104', level: 'district' },
            { name: '青羊区', adcode: '510105', level: 'district' },
            { name: '金牛区', adcode: '510106', level: 'district' },
            { name: '武侯区', adcode: '510107', level: 'district' },
            { name: '成华区', adcode: '510108', level: 'district' },
            { name: '龙泉驿区', adcode: '510112', level: 'district' },
            { name: '青白江区', adcode: '510113', level: 'district' },
            { name: '新都区', adcode: '510114', level: 'district' },
            { name: '温江区', adcode: '510115', level: 'district' },
            { name: '双流区', adcode: '510116', level: 'district' },
            { name: '郫都区', adcode: '510117', level: 'district' },
            { name: '新津区', adcode: '510118', level: 'district' },
            { name: '金堂县', adcode: '510121', level: 'district' },
            { name: '大邑县', adcode: '510129', level: 'district' },
            { name: '蒲江县', adcode: '510131', level: 'district' }
        ]
    };

    return districtData[cityName] || [];
}

// 全局错误处理
window.addEventListener('error', function(e) {
    if (e.message && e.message.includes('AMap')) {
        console.error('高德地图API错误:', e.message);
        // 可以在这里添加错误上报逻辑
    }
});

// 导出配置对象供其他模块使用
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        AMAP_CONFIG,
        ADMIN_DIVISIONS_CACHE,
        POI_SEARCH_CONFIG,
        ADDRESS_VALIDATION_RULES,
        loadProvinces,
        loadCities,
        loadDistricts,
        searchPOI,
        smartAddressSearch,
        validateAddress,
        buildFullAddress,
        clearAdminDivisionsCache,
        getCacheStats
    };
}

