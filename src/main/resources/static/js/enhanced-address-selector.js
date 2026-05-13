/**
 * Enhanced Address Selector Component
 * Implements complete province-city-district three-level linkage, address search and map positioning functions based on Amap API
 *
 * @author Enhanced Address Selector
 * @version 2.0
 */

class EnhancedAddressSelector {
    constructor(options) {
        this.containerId = options.containerId;
        this.onAddressSelected = options.onAddressSelected;
        this.onAddressChange = options.onAddressChange;
        this.amapKey = options.amapKey || '';
        this.defaultCenter = options.defaultCenter || [116.397428, 39.90923];

        // Component state
        this.map = null;
        this.marker = null;
        this.geocoder = null;
        this.districtSearch = null;
        this.poiSearch = null;

        // Address data
        this.provinceList = [];
        this.cityList = [];
        this.districtList = [];
        this.poiSuggestions = [];

        // Selected address
        this.selectedProvince = null;
        this.selectedCity = null;
        this.selectedDistrict = null;
        this.selectedPoi = null;
        this.detailedAddress = '';
        this.longitude = null;
        this.latitude = null;
        this.fullAddress = '';

        // Component state
        this.isLoadingProvinces = false;
        this.isLoadingCities = false;
        this.isLoadingDistricts = false;
        this.isLocating = false;
        this.isSearchingPoi = false;

        this.init();
    }

    init() {
        this.createUI();
        this.initAmap();
        this.loadProvinces();
        this.bindEvents();
    }

    createUI() {
        const container = document.getElementById(this.containerId);
        if (!container) {
            console.error('Container element does not exist: ' + this.containerId);
            return;
        }

        container.innerHTML = `
            <div class="enhanced-address-selector">
                <!-- Province-City-District Three-Level Linkage Selection -->
                <div class="address-selector-section">
                    <h5 class="section-title">
                        <i class="fa fa-map-marker"></i> Administrative Region Selection
                    </h5>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label">Province <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text" id="province-loading">
                                    <i class="fa fa-spinner fa-spin"></i>
                                </span>
                                <select class="form-select" id="province-select" disabled>
                                    <option value="">Please select province</option>
                                </select>
                                <button class="btn btn-outline-secondary btn-sm" type="button" id="refresh-provinces" title="Refresh province list">
                                    <i class="fa fa-refresh"></i>
                                </button>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">City <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text" id="city-loading">
                                    <i class="fa fa-spinner fa-spin"></i>
                                </span>
                                <select class="form-select" id="city-select" disabled>
                                    <option value="">请先选择省份</option>
                                </select>
                                <input type="text" class="form-control" id="city-search" placeholder="搜索城市..." style="display:none;">
                                <button class="btn btn-outline-secondary btn-sm" type="button" id="toggle-city-search" title="切换搜索模式">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">区/县 <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text" id="district-loading">
                                    <i class="fa fa-spinner fa-spin"></i>
                                </span>
                                <select class="form-select" id="district-select" disabled>
                                    <option value="">请先选择城市</option>
                                </select>
                                <input type="text" class="form-control" id="district-search" placeholder="搜索区县..." style="display:none;">
                                <button class="btn btn-outline-secondary btn-sm" type="button" id="toggle-district-search" title="切换搜索模式">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 详细地址输入 -->
                <div class="address-selector-section">
                    <h5 class="section-title">
                        <i class="fa fa-location-arrow"></i> 详细地址
                    </h5>
                    <div class="row g-3">
                        <div class="col-md-9">
                            <label class="form-label">详细地址 <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <input type="text" class="form-control" id="detailed-address"
                                       placeholder="请输入详细地址，如：XX街道XX小区1号楼2单元301"
                                       maxlength="200">
                                <span class="input-group-text" id="char-count">0/200</span>
                            </div>
                            <div class="form-text">
                                <small class="text-muted">
                                    支持中英文、数字及常见标点符号，不支持特殊字符
                                </small>
                            </div>
                            <!-- 地址联想搜索 -->
                            <div class="position-relative mt-2" id="poi-suggestions-container" style="display:none;">
                                <div class="list-group" id="poi-suggestions-list">
                                    <!-- POI搜索结果将动态插入 -->
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">地址操作</label>
                            <div class="d-grid gap-2">
                                <button class="btn btn-primary btn-sm" type="button" id="locate-address">
                                    <i class="fa fa-map-marker"></i> 定位
                                </button>
                                <button class="btn btn-outline-secondary btn-sm" type="button" id="clear-address">
                                    <i class="fa fa-eraser"></i> 清空
                                </button>
                                <button class="btn btn-outline-info btn-sm" type="button" id="current-location">
                                    <i class="fa fa-crosshairs"></i> 当前位置
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 地图展示 -->
                <div class="address-selector-section">
                    <h5 class="section-title">
                        <i class="fa fa-map"></i> 地图定位
                    </h5>
                    <div class="map-container">
                        <div id="address-map" class="address-map" style="width: 100%; height: 400px; min-height: 400px;">
                            <div class="map-loading-overlay">
                                <div class="d-flex flex-column justify-content-center align-items-center h-100">
                                    <div class="spinner-border text-primary" role="status">
                                        <span class="visually-hidden">Loading...</span>
                                    </div>
                                    <div class="mt-2">正在加载地图...</div>
                                </div>
                            </div>
                        </div>
                        <div class="map-controls">
                            <div class="btn-group btn-group-sm" role="group">
                                <button type="button" class="btn btn-outline-secondary" id="zoom-in" title="放大">
                                    <i class="fa fa-plus"></i>
                                </button>
                                <button type="button" class="btn btn-outline-secondary" id="zoom-out" title="缩小">
                                    <i class="fa fa-minus"></i>
                                </button>
                                <button type="button" class="btn btn-outline-secondary" id="reset-map" title="重置视图">
                                    <i class="fa fa-expand"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 地址信息展示 -->
                <div class="address-selector-section">
                    <div class="alert alert-info" id="address-info" style="display:none;">
                        <h6 class="alert-heading">
                            <i class="fa fa-info-circle"></i> 已选地址信息
                        </h6>
                        <div class="address-details">
                            <div class="row">
                                <div class="col-sm-2"><strong>省份:</strong></div>
                                <div class="col-sm-10" id="info-province">-</div>
                            </div>
                            <div class="row">
                                <div class="col-sm-2"><strong>城市:</strong></div>
                                <div class="col-sm-10" id="info-city">-</div>
                            </div>
                            <div class="row">
                                <div class="col-sm-2"><strong>区县:</strong></div>
                                <div class="col-sm-10" id="info-district">-</div>
                            </div>
                            <div class="row">
                                <div class="col-sm-2"><strong>详细地址:</strong></div>
                                <div class="col-sm-10" id="info-detailed">-</div>
                            </div>
                            <div class="row">
                                <div class="col-sm-2"><strong>坐标:</strong></div>
                                <div class="col-sm-10" id="info-coords">-</div>
                            </div>
                            <div class="row">
                                <div class="col-sm-2"><strong>完整地址:</strong></div>
                                <div class="col-sm-10" id="info-full">-</div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 状态提示 -->
                <div class="address-selector-section">
                    <div id="status-message" class="alert" style="display:none;"></div>
                </div>
            </div>
        `;

        // 添加样式
        this.addStyles();
    }

    addStyles() {
        const style = document.createElement('style');
        style.textContent = `
            .enhanced-address-selector {
                font-family: 'Microsoft YaHei', 'Arial', sans-serif;
            }

            .address-selector-section {
                background: #f8f9fa;
                border-radius: 8px;
                padding: 20px;
                margin-bottom: 15px;
                border: 1px solid #e9ecef;
            }

            .section-title {
                color: #495057;
                font-weight: 600;
                margin-bottom: 15px;
                font-size: 1.1rem;
            }

            .input-group-text {
                min-width: 40px;
                text-align: center;
            }

            .position-relative {
                z-index: 1000;
            }

            .list-group {
                max-height: 200px;
                overflow-y: auto;
                border: 1px solid #dee2e6;
                border-radius: 0.375rem;
            }

            .list-group-item {
                cursor: pointer;
                transition: background-color 0.2s;
            }

            .list-group-item:hover {
                background-color: #f8f9fa;
            }

            .map-container {
                position: relative;
                border: 1px solid #dee2e6;
                border-radius: 8px;
                overflow: hidden;
            }

            .map-loading-overlay {
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: rgba(255, 255, 255, 0.9);
                z-index: 1000;
            }

            .map-controls {
                position: absolute;
                top: 10px;
                right: 10px;
                z-index: 1000;
            }

            .text-danger {
                color: #dc3545;
            }

            .btn-sm {
                padding: 0.25rem 0.5rem;
                font-size: 0.875rem;
            }

            @media (max-width: 768px) {
                .address-selector-section {
                    padding: 15px;
                }

                .row.g-3 > div {
                    margin-bottom: 15px;
                }
            }
        `;
        document.head.appendChild(style);
    }

    initAmap() {
        const self = this;

        // 检查高德地图API是否已加载
        if (typeof AMap === 'undefined') {
            this.showMessage('正在加载地图组件...', 'info');
            this.loadAmapScript();
        } else {
            this.initMap();
        }
    }

    loadAmapScript() {
        const script = document.createElement('script');
        script.src = `https://webapi.amap.com/maps?v=2.0&key=${this.amapKey}&plugin=AMap.DistrictSearch,AMap.Geocoder,AMap.AutoComplete,AMap.PlaceSearch`;
        script.onload = () => {
            this.showMessage('地图组件加载成功', 'success');
            this.initMap();
        };
        script.onerror = () => {
            this.showMessage('地图组件加载失败，请检查网络连接', 'error');
        };
        document.head.appendChild(script);
    }

    initMap() {
        const self = this;

        // 初始化地图
        this.map = new AMap.Map('address-map', {
            zoom: 13,
            center: this.defaultCenter,
            resizeEnable: true,
            doubleClickZoom: false
        });

        // 隐藏加载覆盖层
        const loadingOverlay = document.querySelector('.map-loading-overlay');
        if (loadingOverlay) {
            loadingOverlay.style.display = 'none';
        }

        // 初始化插件
        AMap.plugin(['AMap.DistrictSearch', 'AMap.Geocoder', 'AMap.AutoComplete', 'AMap.PlaceSearch'], function() {
            // 行政区查询
            self.districtSearch = new AMap.DistrictSearch({
                level: 'province',
                subdistrict: 1,
                showbiz: false
            });

            // 地理编码
            self.geocoder = new AMap.Geocoder({
                city: '全国'
            });

            // POI搜索
            self.poiSearch = new AMap.PlaceSearch({
                city: '全国',
                pageSize: 10
            });

            // 自动补全
            const autoOptions = {
                input: "detailed-address"
            };
            const auto = new AMap.AutoComplete(autoOptions);

            // 地图点击事件
            self.map.on('click', function(e) {
                self.setMarker(e.lnglat.lng, e.lnglat.lat);
                self.reverseGeocode(e.lnglat.lng, e.lnglat.lat);
            });
        });

        // 初始化地图控件
        this.initMapControls();
    }

    initMapControls() {
        // 放大按钮
        document.getElementById('zoom-in').addEventListener('click', () => {
            this.map.zoomIn();
        });

        // 缩小按钮
        document.getElementById('zoom-out').addEventListener('click', () => {
            this.map.zoomOut();
        });

        // 重置视图按钮
        document.getElementById('reset-map').addEventListener('click', () => {
            this.map.setCenter(this.defaultCenter);
            this.map.setZoom(13);
        });
    }

    bindEvents() {
        this.bindProvinceEvents();
        this.bindCityEvents();
        this.bindDistrictEvents();
        this.bindDetailedAddressEvents();
        this.bindMapControlEvents();
        this.bindSearchEvents();
    }

    bindProvinceEvents() {
        const provinceSelect = document.getElementById('province-select');
        const refreshBtn = document.getElementById('refresh-provinces');

        provinceSelect.addEventListener('change', (e) => {
            const value = e.target.value;
            if (value) {
                this.selectProvince(value);
            } else {
                this.clearCityAndDistrict();
            }
        });

        refreshBtn.addEventListener('click', () => {
            this.loadProvinces(true);
        });
    }

    bindCityEvents() {
        const citySelect = document.getElementById('city-select');
        const citySearch = document.getElementById('city-search');
        const toggleSearchBtn = document.getElementById('toggle-city-search');

        citySelect.addEventListener('change', (e) => {
            const value = e.target.value;
            if (value) {
                this.selectCity(value);
            } else {
                this.clearDistrict();
            }
        });

        toggleSearchBtn.addEventListener('click', () => {
            this.toggleCitySearchMode();
        });

        citySearch.addEventListener('input', (e) => {
            this.filterCityList(e.target.value);
        });

        citySearch.addEventListener('change', (e) => {
            const value = e.target.value;
            const matchedCity = this.cityList.find(city =>
                city.name.includes(value) || city.adcode === value
            );
            if (matchedCity) {
                this.selectCity(matchedCity.name);
            }
        });
    }

    bindDistrictEvents() {
        const districtSelect = document.getElementById('district-select');
        const districtSearch = document.getElementById('district-search');
        const toggleSearchBtn = document.getElementById('toggle-district-search');

        districtSelect.addEventListener('change', (e) => {
            const value = e.target.value;
            if (value) {
                this.selectDistrict(value);
            }
        });

        toggleSearchBtn.addEventListener('click', () => {
            this.toggleDistrictSearchMode();
        });

        districtSearch.addEventListener('input', (e) => {
            this.filterDistrictList(e.target.value);
        });

        districtSearch.addEventListener('change', (e) => {
            const value = e.target.value;
            const matchedDistrict = this.districtList.find(district =>
                district.name.includes(value) || district.adcode === value
            );
            if (matchedDistrict) {
                this.selectDistrict(matchedDistrict.name);
            }
        });
    }

    bindDetailedAddressEvents() {
        const detailedInput = document.getElementById('detailed-address');
        const charCount = document.getElementById('char-count');

        detailedInput.addEventListener('input', (e) => {
            const value = e.target.value;
            this.detailedAddress = value;

            // 更新字符计数
            charCount.textContent = `${value.length}/200`;

            // 输入验证
            if (this.validateDetailedAddress(value)) {
                detailedInput.classList.remove('is-invalid');
                detailedInput.classList.add('is-valid');
            } else {
                detailedInput.classList.remove('is-valid');
                detailedInput.classList.add('is-invalid');
            }

            // POI搜索
            if (this.selectedProvince && this.selectedCity && this.selectedDistrict && value.length >= 2) {
                this.searchPoi(value);
            } else {
                this.hidePoiSuggestions();
            }

            this.triggerAddressChange();
        });

        detailedInput.addEventListener('blur', () => {
            this.validateDetailedAddress(this.detailedAddress);
        });
    }

    bindMapControlEvents() {
        document.getElementById('locate-address').addEventListener('click', () => {
            this.locateAddress();
        });

        document.getElementById('clear-address').addEventListener('click', () => {
            this.clearAll();
        });

        document.getElementById('current-location').addEventListener('click', () => {
            this.getCurrentLocation();
        });
    }

    bindSearchEvents() {
        // POI建议项点击事件
        document.addEventListener('click', (e) => {
            if (e.target.matches('.poi-suggestion-item')) {
                const poiData = JSON.parse(e.target.dataset.poi);
                this.selectPoi(poiData);
            }
        });
    }

    // 加载省份数据
    loadProvinces(isRefresh = false) {
        const self = this;

        this.setLoadingState('provinces', true);

        if (isRefresh) {
            this.showMessage('正在刷新省份列表...', 'info');
            // 清除缓存
            if (typeof clearAdminDivisionsCache === 'function') {
                clearAdminDivisionsCache();
            }
        }

        // 使用map-config.js中的缓存加载函数
        if (typeof loadProvinces === 'function') {
            loadProvinces((provinces) => {
                self.setLoadingState('provinces', false);

                self.provinceList = provinces.map(province => ({
                    name: province.name,
                    adcode: province.adcode,
                    level: province.level
                }));

                self.populateProvinceSelect();
                self.showMessage('省份列表加载成功', 'success');
            });
        } else {
            // 备用方案：直接使用高德API
            this.districtSearch.search('中国', (status, result) => {
                self.setLoadingState('provinces', false);

                if (status === 'complete') {
                    self.provinceList = result.districtList[0].districtList.map(province => ({
                        name: province.name,
                        adcode: province.adcode,
                        level: province.level
                    }));

                    self.populateProvinceSelect();
                    self.showMessage('省份列表加载成功', 'success');
                } else {
                    self.showMessage('省份列表加载失败', 'error');
                    if (isRefresh) {
                        self.showRetryButton(() => self.loadProvinces(true));
                    }
                }
            });
        }
    }

    populateProvinceSelect() {
        const select = document.getElementById('province-select');
        select.innerHTML = '<option value="">请选择省份</option>';

        // 按名称排序
        this.provinceList.sort((a, b) => a.name.localeCompare(b.name, 'zh-CN'));

        this.provinceList.forEach(province => {
            const option = document.createElement('option');
            option.value = province.name;
            option.textContent = province.name;
            select.appendChild(option);
        });

        select.disabled = false;
    }

    selectProvince(provinceName) {
        const self = this;
        const province = this.provinceList.find(p => p.name === provinceName);

        if (!province) return;

        this.selectedProvince = province;
        this.clearCityAndDistrict();
        this.loadCities(provinceName);

        // 检查是否为直辖市
        const isDirectCity = this.isDirectCity(provinceName);
        if (isDirectCity) {
            this.selectCity(provinceName);
        }

        this.triggerAddressChange();
    }

    loadCities(provinceName) {
        const self = this;

        this.setLoadingState('cities', true);

        // 使用map-config.js中的缓存加载函数
        if (typeof loadCities === 'function') {
            loadCities(provinceName, (cities) => {
                self.setLoadingState('cities', false);

                self.cityList = cities.map(city => ({
                    name: city.name,
                    adcode: city.adcode,
                    level: city.level
                }));

                self.populateCitySelect();

                // 如果是直辖市且已选择，则自动加载区县
                if (self.isDirectCity(provinceName) && self.selectedCity) {
                    self.loadDistricts(self.selectedCity.name);
                }
            });
        } else {
            // 备用方案：直接使用高德API
            this.districtSearch.setLevel('city');
            this.districtSearch.search(provinceName, (status, result) => {
                self.setLoadingState('cities', false);

                if (status === 'complete') {
                    self.cityList = result.districtList[0].districtList.map(city => ({
                        name: city.name,
                        adcode: city.adcode,
                        level: city.level
                    }));

                    self.populateCitySelect();

                    // 如果是直辖市且已选择，则自动加载区县
                    if (self.isDirectCity(provinceName) && self.selectedCity) {
                        self.loadDistricts(self.selectedCity.name);
                    }
                } else {
                    self.showMessage('城市列表加载失败', 'error');
                }
            });
        }
    }

    populateCitySelect() {
        const select = document.getElementById('city-select');
        const searchInput = document.getElementById('city-search');

        select.innerHTML = '<option value="">请选择城市</option>';

        // 按名称排序
        this.cityList.sort((a, b) => a.name.localeCompare(b.name, 'zh-CN'));

        this.cityList.forEach(city => {
            const option = document.createElement('option');
            option.value = city.name;
            option.textContent = city.name;
            select.appendChild(option);
        });

        select.disabled = false;
        searchInput.style.display = 'none';
        select.style.display = 'block';
        document.getElementById('toggle-city-search').innerHTML = '<i class="fa fa-search"></i>';
    }

    selectCity(cityName) {
        const self = this;
        const city = this.cityList.find(c => c.name === cityName);

        if (!city) return;

        this.selectedCity = city;
        this.clearDistrict();
        this.loadDistricts(cityName);
        this.triggerAddressChange();
    }

    loadDistricts(cityName) {
        const self = this;

        this.setLoadingState('districts', true);

        // 使用map-config.js中的缓存加载函数
        if (typeof loadDistricts === 'function') {
            loadDistricts(cityName, (districts) => {
                self.setLoadingState('districts', false);

                self.districtList = districts.map(district => ({
                    name: district.name,
                    adcode: district.adcode,
                    level: district.level
                }));

                self.populateDistrictSelect();
            });
        } else {
            // 备用方案：直接使用高德API
            this.districtSearch.setLevel('district');
            this.districtSearch.search(cityName, (status, result) => {
                self.setLoadingState('districts', false);

                if (status === 'complete') {
                    self.districtList = result.districtList[0].districtList.map(district => ({
                        name: district.name,
                        adcode: district.adcode,
                        level: district.level
                    }));

                    self.populateDistrictSelect();
                } else {
                    self.showMessage('区县列表加载失败', 'error');

                    // 检查是否为无下级行政区
                    if (self.districtList.length === 0) {
                        self.handleNoDistricts();
                    }
                }
            });
        }
    }

    populateDistrictSelect() {
        const select = document.getElementById('district-select');
        const searchInput = document.getElementById('district-search');

        select.innerHTML = '<option value="">请选择区/县</option>';

        // 按名称排序
        this.districtList.sort((a, b) => a.name.localeCompare(b.name, 'zh-CN'));

        this.districtList.forEach(district => {
            const option = document.createElement('option');
            option.value = district.name;
            option.textContent = district.name;
            select.appendChild(option);
        });

        select.disabled = false;
        searchInput.style.display = 'none';
        select.style.display = 'block';
        document.getElementById('toggle-district-search').innerHTML = '<i class="fa fa-search"></i>';
    }

    selectDistrict(districtName) {
        const district = this.districtList.find(d => d.name === districtName);

        if (!district) return;

        this.selectedDistrict = district;
        this.triggerAddressChange();

        // 如果已有详细地址，尝试自动定位
        if (this.detailedAddress) {
            this.locateAddress();
        }
    }

    toggleCitySearchMode() {
        const select = document.getElementById('city-select');
        const searchInput = document.getElementById('city-search');
        const toggleBtn = document.getElementById('toggle-city-search');

        if (searchInput.style.display === 'none') {
            // 切换到搜索模式
            select.style.display = 'none';
            searchInput.style.display = 'block';
            toggleBtn.innerHTML = '<i class="fa fa-list"></i>';
            searchInput.focus();
        } else {
            // 切换到下拉模式
            searchInput.style.display = 'none';
            select.style.display = 'block';
            toggleBtn.innerHTML = '<i class="fa fa-search"></i>';
        }
    }

    toggleDistrictSearchMode() {
        const select = document.getElementById('district-select');
        const searchInput = document.getElementById('district-search');
        const toggleBtn = document.getElementById('toggle-district-search');

        if (searchInput.style.display === 'none') {
            // 切换到搜索模式
            select.style.display = 'none';
            searchInput.style.display = 'block';
            toggleBtn.innerHTML = '<i class="fa-list"></i>';
            searchInput.focus();
        } else {
            // 切换到下拉模式
            searchInput.style.display = 'none';
            select.style.display = 'block';
            toggleBtn.innerHTML = '<i class="fa fa-search"></i>';
        }
    }

    filterCityList(keyword) {
        if (!keyword) {
            this.populateCitySelect();
            return;
        }

        const filteredList = this.cityList.filter(city =>
            city.name.includes(keyword) || city.adcode === keyword
        );

        const select = document.getElementById('city-select');
        select.innerHTML = '<option value="">请选择城市</option>';

        filteredList.forEach(city => {
            const option = document.createElement('option');
            option.value = city.name;
            option.textContent = city.name;
            select.appendChild(option);
        });
    }

    filterDistrictList(keyword) {
        if (!keyword) {
            this.populateDistrictSelect();
            return;
        }

        const filteredList = this.districtList.filter(district =>
            district.name.includes(keyword) || district.adcode === keyword
        );

        const select = document.getElementById('district-select');
        select.innerHTML = '<option value="">请选择区/县</option>';

        filteredList.forEach(district => {
            const option = document.createElement('option');
            option.value = district.name;
            option.textContent = district.name;
            select.appendChild(option);
        });
    }

    validateDetailedAddress(address) {
        // 基本验证：不能为空，不能包含特殊字符
        if (!address || address.trim().length === 0) {
            return false;
        }

        // 长度验证
        if (address.length > 200) {
            return false;
        }

        // 特殊字符验证
        const invalidChars = /[@$%^&*()+=\[\]{}|\\;:'",.<>\/?]/;
        if (invalidChars.test(address)) {
            return false;
        }

        return true;
    }

    searchPoi(keyword) {
        const self = this;

        if (!this.selectedProvince || !this.selectedCity || !this.selectedDistrict) {
            return;
        }

        this.setLoadingState('poi', true);
        this.isSearchingPoi = true;

        // 构建上下文信息
        const context = {
            province: this.selectedProvince.name,
            city: this.selectedCity.name,
            district: this.selectedDistrict.name
        };

        // 使用map-config.js中的智能地址搜索
        if (typeof smartAddressSearch === 'function') {
            smartAddressSearch(keyword, context, (success, pois) => {
                self.setLoadingState('poi', false);
                self.isSearchingPoi = false;

                if (success && pois.length > 0) {
                    self.poiSuggestions = pois;
                    self.showPoiSuggestions();
                } else {
                    self.hidePoiSuggestions();
                }
            });
        } else {
            // 备用方案：直接使用高德POI搜索
            const searchCity = this.selectedProvince.name === this.selectedCity.name ?
                this.selectedCity.name : `${this.selectedProvince.name}${this.selectedCity.name}`;

            this.poiSearch.setCity(searchCity);
            this.poiSearch.search(keyword, (status, result) => {
                self.setLoadingState('poi', false);
                self.isSearchingPoi = false;

                if (status === 'complete' && result.poiList && result.poiList.length > 0) {
                    self.poiSuggestions = result.poiList.pois || result.poiList;
                    self.showPoiSuggestions();
                } else {
                    self.hidePoiSuggestions();
                }
            });
        }
    }

    showPoiSuggestions() {
        const container = document.getElementById('poi-suggestions-container');
        const list = document.getElementById('poiSuggestions-list');

        list.innerHTML = '';

        this.poiSuggestions.forEach(poi => {
            const item = document.createElement('a');
            item.className = 'list-group-item list-group-item-action poi-suggestion-item';
            item.href = '#';
            item.dataset.poi = JSON.stringify({
                name: poi.name,
                address: poi.address,
                location: poi.location
            });

            item.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <strong>${poi.name}</strong>
                        <br>
                        <small class="text-muted">${poi.address}</small>
                    </div>
                    <small class="text-muted">${poi.category}</small>
                </div>
            `;

            list.appendChild(item);
        });

        container.style.display = 'block';
    }

    hidePoiSuggestions() {
        const container = document.getElementById('poi-suggestions-container');
        container.style.display = 'none';
        this.poiSuggestions = [];
    }

    selectPoi(poiData) {
        this.selectedPoi = poiData;
        this.detailedAddress = poiData.address;
        document.getElementById('detailed-address').value = poiData.address;

        // 更新字符计数
        const charCount = document.getElementById('char-count');
        charCount.textContent = `${poiData.address.length}/200`;

        this.hidePoiSuggestions();
        this.setMarker(poiData.location.lng, poiData.location.lat);
        this.map.setCenter([poiData.location.lng, poiData.location.lat]);
        this.map.setZoom(18);

        this.triggerAddressChange();
    }

    locateAddress() {
        const self = this;

        // 验证必填项
        if (!this.selectedProvince || !this.selectedCity || !this.selectedDistrict) {
            this.showMessage('请先选择省市区', 'warning');
            return;
        }

        if (!this.detailedAddress || !this.detailedAddress.trim()) {
            this.showMessage('请输入详细地址', 'warning');
            return;
        }

        this.setLoadingState('locate', true);
        this.isLocating = true;

        const fullAddress = this.buildFullAddress();

        this.geocoder.getLocation(fullAddress, (status, result) => {
            self.setLoadingState('locate', false);
            self.isLocating = false;

            if (status === 'complete' && result.geocodes && result.geocodes.length > 0) {
                const location = result.geocodes[0].location;
                const addressComponent = result.geocodes[0].addressComponent;

                self.setMarker(location.lng, location.lat);
                self.map.setCenter([location.lng, location.lat]);
                self.map.setZoom(18);

                // 更新地址信息
                self.updateAddressInfo();

                // 触发地址选择事件
                self.triggerAddressSelected();

                self.showMessage('定位成功', 'success');
            } else {
                self.showMessage('地址定位失败，请检查地址是否正确', 'error');

                // 尝试使用省市区中心定位
                self.locateToDistrictCenter();
            }
        });
    }

    locateToDistrictCenter() {
        if (!this.selectedDistrict) return;

        this.districtSearch.setLevel('district');
        this.districtSearch.search(this.selectedDistrict.name, (status, result) => {
            if (status === 'complete' && result.districtList && result.districtList.length > 0) {
                const district = result.districtList[0];
                const center = district.center;

                this.setMarker(center.lng, center.lat);
                this.map.setCenter([center.lng, center.lat]);
                this.map.setZoom(12);

                this.showMessage('已定位到区中心', 'info');
            }
        });
    }

    getCurrentLocation() {
        const self = this;

        if (!navigator.geolocation) {
            self.showMessage('浏览器不支持定位功能', 'warning');
            return;
        }

        this.showMessage('正在获取当前位置...', 'info');

        navigator.geolocation.getCurrentPosition(
            (position) => {
                const { latitude, longitude } = position.coords;

                self.setMarker(longitude, latitude);
                self.map.setCenter([longitude, latitude]);
                self.map.setZoom(15);

                // 反向地理编码获取地址信息
                self.reverseGeocode(longitude, latitude);

                self.showMessage('定位成功', 'success');
            },
            (error) => {
                self.showMessage('定位失败，请检查定位权限', 'error');
            }
        );
    }

    reverseGeocode(lng, lat) {
        const self = this;

        this.geocoder.getAddress([lng, lat], (status, result) => {
            if (status === 'complete' && result.regeocode) {
                const addressComponent = result.regeocode.addressComponent;
                const formattedAddress = result.regeocode.formattedAddress;

                // 更新省市区选择
                this.updateAddressFromGeocode(addressComponent);

                // 更新详细地址
                if (!this.detailedAddress) {
                    this.detailedAddress = formattedAddress;
                    document.getElementById('detailed-address').value = formattedAddress;
                    this.updateCharCount();
                }

                this.triggerAddressChange();
            }
        });
    }

    updateAddressFromGeocode(addressComponent) {
        const province = addressComponent.province;
        const city = addressComponent.city || addressComponent.province; // 直辖市处理
        const district = addressComponent.district;

        // 更新省份
        if (province && this.provinceList.find(p => p.name === province)) {
            this.selectProvince(province);
            document.getElementById('province-select').value = province;
        }

        // 更新城市
        if (city && this.cityList.find(c => c.name === city)) {
            setTimeout(() => {
                this.selectCity(city);
                document.getElementById('city-select').value = city;
            }, 300);
        }

        // 更新区县
        if (district && this.districtList.find(d => d.name === district)) {
            setTimeout(() => {
                this.selectDistrict(district);
                document.getElementById('district-select').value = district;
            }, 600);
        }
    }

    setMarker(lng, lat) {
        if (this.marker) {
            this.marker.setPosition([lng, lat]);
        } else {
            this.marker = new AMap.Marker({
                position: [lng, lat],
                map: this.map,
                icon: new AMap.Icon({
                    image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png',
                    size: new AMap.Size(25, 34),
                    imageSize: new AMap.Size(25, 34)
                }),
                animation: "AMAP_ANIMATION_DROP"
            });
        }

        this.longitude = lng;
        this.latitude = lat;
    }

    buildFullAddress() {
        const parts = [];

        if (this.selectedProvince) {
            parts.push(this.selectedProvince.name);
        }

        if (this.selectedCity) {
            // 避免直辖市重复
            if (this.selectedCity.name !== this.selectedProvince.name) {
                parts.push(this.selectedCity.name);
            }
        }

        if (this.selectedDistrict) {
            parts.push(this.selectedDistrict.name);
        }

        if (this.detailedAddress) {
            parts.push(this.detailedAddress);
        }

        return parts.join('');
    }

    updateAddressInfo() {
        const infoDiv = document.getElementById('address-info');

        if (this.selectedProvince || this.selectedCity || this.selectedDistrict || this.detailedAddress) {
            infoDiv.style.display = 'block';

            document.getElementById('info-province').textContent = this.selectedProvince ? this.selectedProvince.name : '-';
            document.getElementById('info-city').textContent = this.selectedCity ? this.selectedCity.name : '-';
            document.getElementById('info-district').textContent = this.selectedDistrict ? this.selectedDistrict.name : '-';
            document.getElementById('info-detailed').textContent = this.detailedAddress || '-';
            document.getElementById('info-coords').textContent =
                this.longitude && this.latitude ?
                `${this.longitude.toFixed(6)}, ${this.latitude.toFixed(6)}` : '-';
            document.getElementById('info-full').textContent = this.buildFullAddress();
        } else {
            infoDiv.style.display = 'none';
        }
    }

    clearCityAndDistrict() {
        this.selectedCity = null;
        this.selectedDistrict = null;
        this.cityList = [];
        this.districtList = [];

        const citySelect = document.getElementById('city-select');
        const districtSelect = document.getElementById('district-select');

        citySelect.innerHTML = '<option value="">请先选择省份</option>';
        citySelect.disabled = true;

        districtSelect.innerHTML = '<option value="">请先选择城市</option>';
        districtSelect.disabled = true;

        // 隐藏搜索模式
        document.getElementById('city-search').style.display = 'none';
        document.getElementById('district-search').style.display = 'none';
        document.getElementById('toggle-city-search').innerHTML = '<i class="fa fa-search"></i>';
        document.getElementById('toggle-district-search').innerHTML = '<i class="fa fa-search"></i>';
    }

    clearDistrict() {
        this.selectedDistrict = null;
        this.districtList = [];

        const districtSelect = document.getElementById('district-select');
        districtSelect.innerHTML = '<option value="">请先选择城市</option>';
        districtSelect.disabled = true;

        // 隐藏搜索模式
        document.getElementById('district-search').style.display = 'none';
        document.getElementById('toggle-district-search').innerHTML = '<i class="fa-search"></i>';
    }

    clearAll() {
        this.selectedProvince = null;
        this.selectedCity = null;
        this.selectedDistrict = null;
        this.selectedPoi = null;
        this.detailedAddress = '';
        this.longitude = null;
        this.latitude = null;

        // 清空表单
        document.getElementById('province-select').value = '';
        document.getElementById('city-select').value = '';
        document.getElementById('district-select').value = '';
        document.getElementById('detailed-address').value = '';
        document.getElementById('char-count').textContent = '0/200';

        // 重置状态
        this.clearCityAndDistrict();

        // 清除地图标记
        if (this.marker) {
            this.marker.setMap(null);
            this.marker = null;
        }

        // 重置地图视图
        this.map.setCenter(this.defaultCenter);
        this.map.setZoom(13);

        // 隐藏地址信息
        document.getElementById('address-info').style.display = 'none';

        // 隐藏POI建议
        this.hidePoiSuggestions();

        // 重置表单样式
        document.getElementById('detailed-address').classList.remove('is-valid', 'is-invalid');

        this.showMessage('已清空所有地址信息', 'info');
        this.triggerAddressChange();
    }

    isDirectCity(provinceName) {
        const directCities = ['北京市', '上海市', '天津市', '重庆市'];
        return directCities.includes(provinceName);
    }

    handleNoDistricts() {
        const self = this;

        // 对于无下级行政区的情况，自动选择第一个区（或显示提示）
        if (this.districtList.length === 0) {
            // 这里可以根据需求处理，比如直接选择城市本身或者显示特殊提示
            this.showMessage('该城市无下级区县', 'info');

            // 自动选择城市作为区县（适用于县级市）
            if (this.selectedCity) {
                this.selectedDistrict = {
                    name: this.selectedCity.name,
                    adcode: this.selectedCity.adcode,
                    level: this.selectedCity.level
                };

                // 更新区县下拉框
                const districtSelect = document.getElementById('district-select');
                districtSelect.innerHTML = `<option value="${this.selectedCity.name}" selected>${this.selectedCity.name}</option>`;
                districtSelect.disabled = false;

                this.triggerAddressChange();
            }
        }
    }

    setLoadingState(type, loading) {
        const loadingElement = document.getElementById(`${type}-loading`);
        const selectElement = document.getElementById(`${type}-select`);
        const searchElement = document.getElementById(`${type}-search`);

        if (loading) {
            loadingElement.style.display = 'inline-block';
            if (selectElement) selectElement.disabled = true;
            if (searchElement) searchElement.disabled = true;
        } else {
            loadingElement.style.display = 'none';
            if (selectElement && this.provinceList.length > 0) selectElement.disabled = false;
            if (searchElement && this.cityList.length > 0) searchElement.disabled = false;
        }
    }

    showMessage(message, type = 'info') {
        const messageDiv = document.getElementById('status-message');

        messageDiv.className = `alert alert-${type}`;
        messageDiv.style.display = 'block';

        const iconMap = {
            'success': 'fa-check-circle',
            'error': 'fa-exclamation-triangle',
            'warning': 'fa-exclamation-circle',
            'info': 'fa-info-circle'
        };

        messageDiv.innerHTML = `
            <i class="fa ${iconMap[type]}"></i> ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        `;

        // 自动隐藏
        setTimeout(() => {
            messageDiv.style.display = 'none';
        }, 5000);

        // 点击关闭按钮事件
        messageDiv.querySelector('.btn-close').addEventListener('click', () => {
            messageDiv.style.display = 'none';
        });
    }

    showRetryButton(retryCallback) {
        const messageDiv = document.getElementById('status-message');
        const retryBtn = document.createElement('button');
        retryBtn.className = 'btn btn-warning btn-sm mt-2';
        retryBtn.textContent = '重试';
        retryBtn.addEventListener('click', retryCallback);
        messageDiv.appendChild(retryBtn);
    }

    updateCharCount() {
        const input = document.getElementById('detailed-address');
        const charCount = document.getElementById('char-count');
        charCount.textContent = `${input.value.length}/200`;
    }

    triggerAddressChange() {
        const addressData = {
            province: this.selectedProvince,
            city: this.selectedCity,
            district: this.selectedDistrict,
            detailedAddress: this.detailedAddress,
            longitude: this.longitude,
            latitude: this.latitude,
            fullAddress: this.buildFullAddress()
        };

        if (this.onAddressChange) {
            this.onAddressChange(addressData);
        }

        // 更新地址信息显示
        this.updateAddressInfo();
    }

    triggerAddressSelected() {
        const addressData = {
            province: this.selectedProvince,
            city: this.selectedCity,
            district: this.selectedDistrict,
            detailedAddress: this.detailedAddress,
            longitude: this.longitude,
            latitude: this.latitude,
            fullAddress: this.buildFullAddress()
        };

        if (this.onAddressSelected) {
            this.onAddressSelected(addressData);
        }
    }

    // 公共方法：设置地址
    setAddress(addressData) {
        if (addressData.province) {
            this.selectProvince(addressData.province);
            document.getElementById('province-select').value = addressData.province;
        }

        if (addressData.city) {
            setTimeout(() => {
                this.selectCity(addressData.city);
                document.getElementById('city-select').value = addressData.city;
            }, 300);
        }

        if (addressData.district) {
            setTimeout(() => {
                this.selectDistrict(addressData.district);
                document.getElementById('district-select').value = addressData.district;
            }, 600);
        }

        if (addressData.detailedAddress) {
            this.detailedAddress = addressData.detailedAddress;
            document.getElementById('detailed-address').value = addressData.detailedAddress;
            this.updateCharCount();
        }

        if (addressData.longitude && addressData.latitude) {
            this.setMarker(addressData.longitude, addressData.latitude);
            this.map.setCenter([addressData.longitude, addressData.latitude]);
            this.map.setZoom(15);
        }

        this.triggerAddressChange();
        this.triggerAddressSelected();
    }

    // 公共方法：获取地址数据
    getAddressData() {
        return {
            province: this.selectedProvince,
            city: this.selectedCity,
            district: this.selectedDistrict,
            detailedAddress: this.detailedAddress,
            longitude: this.longitude,
            latitude: this.latitude,
            fullAddress: this.buildFullAddress()
        };
    }

    // 公共方法：验证地址完整性
    validateAddress() {
        // 构建地址数据对象
        const addressData = {
            province: this.selectedProvince ? this.selectedProvince.name : '',
            city: this.selectedCity ? this.selectedCity.name : '',
            district: this.selectedDistrict ? this.selectedDistrict.name : '',
            detailedAddress: this.detailedAddress,
            longitude: this.longitude,
            latitude: this.latitude
        };

        // 使用map-config.js中的验证函数
        if (typeof validateAddress === 'function') {
            const validationResult = validateAddress(addressData);

            // 显示验证结果
            this.displayValidationResult(validationResult);

            return validationResult.isValid;
        } else {
            // 备用验证方案
            return this.selectedProvince &&
                   this.selectedCity &&
                   this.selectedDistrict &&
                   this.detailedAddress &&
                   this.validateDetailedAddress(this.detailedAddress);
        }
    }

    // 显示验证结果
    displayValidationResult(result) {
        const messageDiv = document.getElementById('status-message');

        if (result.level === 'error') {
            this.showMessage(result.errors.join('；'), 'error');
        } else if (result.level === 'warning') {
            this.showMessage(result.warnings.join('；'), 'warning');
        } else if (result.level === 'success') {
            this.showMessage('地址信息完整有效', 'success');
        }
    }

    // 公共方法：销毁组件
    destroy() {
        // 清理地图
        if (this.map) {
            this.map.destroy();
            this.map = null;
        }

        // 清理事件监听器
        // ... 清理所有绑定的事件
    }
}

// 导出组件
window.EnhancedAddressSelector = EnhancedAddressSelector;