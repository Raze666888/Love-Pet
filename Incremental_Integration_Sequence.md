# Pet Service Platform — Incremental Integration Sequence Definition

> This document applies the **Incremental Integration Sequence** methodology from systems engineering to the Pet Service Platform project, defining integration increments from baseline to full capability.

---

# Version 1: Full System Integration (Greenfield Development Scenario)

> ⚠️ **Important Note**: This version applies to **building the entire system from scratch**
> - Total of **14 business modules** to be integrated
> - KPP metrics measure coverage across all 14 modules
>
> For comparison with Version 2 (incremental development on existing system), see below.

---

## I. KPP (Key Performance Parameters) Definition

This project adopts a **dual-dimension KPP** approach:

| Dimension | KPP Metric | Meaning |
|-----------|------------|---------|
| **Business Function Coverage** | Number of integrated and verified business modules / Total modules (14) | Measures functional completeness |
| **System Performance Metrics** | Response time ≤ 200ms, Concurrent support ≥ 100, Availability ≥ 99% | Measures system readiness |

---

## II. Figure 1: Top-Level KPP & Key Functionality Milestones (Timeline)

> Left to right, progressing over time. Each milestone indicates business coverage and performance achievement.

```mermaid
gantt
    title Pet Service Platform — Incremental Integration Milestone Timeline
    dateFormat  YYYY-MM-DD
    axisFormat  %m/%d

    section M1: Basic Exposure
    User registration/login available        :m1a, 2025-09-01, 5d
    Service provider onboarding available    :m1b, after m1a, 5d
    Milestone: Basic Exposure & Acquisition  :milestone, m1, after m1b, 0d

    section M2: First Order Manual Prep
    Product/service browsing & search        :m2a, after m1, 7d
    Shopping cart (manual order)             :m2b, after m2a, 5d
    Order creation (manual process)          :m2c, after m2b, 5d
    Milestone: First Order Manual Prep       :milestone, m2, after m2c, 0d

    section M3: 10% Function + Basic Performance
    Recharge & balance payment               :m3a, after m2, 5d
    Internal message notification            :m3b, after m3a, 3d
    Milestone: 10% Function · Basic Perf     :milestone, m3, after m3b, 0d

    section M4: 20% Function + Automation
    Order evaluation system                  :m4a, after m3, 5d
    User favorites feature                   :m4b, after m4a, 3d
    Website announcements & comments         :m4c, after m4b, 3d
    Milestone: 20% Function · Automation     :milestone, m4, after m4c, 0d

    section M5: 50% Function + Performance Optimization
    Service provider dashboard               :m5a, after m4, 7d
    Address selection & distance filtering   :m5b, after m5a, 5d
    Performance optimization (Redis/cache)   :m5c, after m5b, 5d
    Milestone: 50% Function · Perf Opt       :milestone, m5, after m5c, 0d

    section M6: Full IQ · Full Speed
    Full module integration testing          :m6a, after m5, 7d
    Automated test coverage                  :m6b, after m6a, 5d
    Docker containerized deployment          :m6c, after m6b, 5d
    Monitoring alerts (Prometheus)           :m6d, after m6c, 3d
    Milestone: Full IQ · Full Speed          :milestone, m6, after m6d, 0d
```

### Milestone KPP Achievement Table

| Milestone | Business Function Coverage | Performance Achievement | Integrated Modules |
|-----------|---------------------------|------------------------|-------------------|
| **M1: Basic Exposure & Acquisition** | ~15% (2/14) | N/A (functional verification phase) | login, sysuser, company |
| **M2: First Order Manual Prep** | ~35% (5/14) | Basic availability | + product, producttype, shopcart |
| **M3: 10% Function · Basic Performance** | ~50% (7/14) | Response ≤ 500ms | + order, tmoney, message |
| **M4: 20% Function · Automation** | ~75% (11/14) | Response ≤ 300ms | + orderEvalute, userlike, webnotice, comment |
| **M5: 50% Function · Performance Optimization** | ~85% (12/14) | Response ≤ 200ms, Concurrent 50 | + provider, address/distance features |
| **M6: Full IQ · Full Speed** | **100% (14/14)** | **Response ≤ 200ms, Concurrent ≥ 100, Availability ≥ 99%** | + Full integration testing, Docker deployment, Prometheus monitoring |

---

## III. Figure 2: Demand-Driven Backward Planning

> Working backward from the final goal (M6: Full IQ / Full Speed), decomposing into verifiable integration increments.

```mermaid
flowchart TB
    subgraph TARGET["🎯 Final Goal: M6 — Full IQ · Full Speed"]
        M6["M6: Full Module Integration + Full Performance Achievement\n100% Function · Response ≤ 200ms · Concurrent ≥ 100\nDocker Deployment · Prometheus Monitoring · CI/CD"]
    end

    subgraph M5_BLOCK["M5: 50% Function · Performance Optimization"]
        M5["M5: Provider Dashboard + Distance Filter + Redis Cache\n85% Function · Response ≤ 200ms · Concurrent 50"]
    end

    subgraph M4_BLOCK["M4: 20% Function · Automation"]
        M4["M4: Evaluation + Favorites + Announcements + Comments\n75% Function · Response ≤ 300ms"]
    end

    subgraph M3_BLOCK["M3: 10% Function · Basic Performance"]
        M3["M3: Recharge Payment + Message Notification + Orders\n50% Function · Response ≤ 500ms"]
    end

    subgraph M2_BLOCK["M2: First Order Manual Prep"]
        M2["M2: Product Browsing + Shopping Cart + Order Placement\n35% Function · Basic Availability"]
    end

    subgraph M1_BLOCK["M1: Basic Exposure & Acquisition"]
        M1["M1: Registration/Login + Provider Onboarding\n15% Function · Functional Verification"]
    end

    M6 -->|"Requires: All modules ready + Performance achieved"| M5
    M5 -->|"Requires: Core chain complete + Cache layer"| M4
    M4 -->|"Requires: Payment loop + Notification mechanism"| M3
    M3 -->|"Requires: Products browsable + Shopping flow"| M2
    M2 -->|"Requires: User system + Provider system"| M1

    style TARGET fill:#1a73e8,stroke:#0d47a1,color:#fff,stroke-width:3px
    style M5_BLOCK fill:#4285f4,stroke:#1a73e8,color:#fff
    style M4_BLOCK fill:#5e97f6,stroke:#4285f4,color:#fff
    style M3_BLOCK fill:#7baaf7,stroke:#5e97f6,color:#fff
    style M2_BLOCK fill:#a4c2f4,stroke:#7baaf7,color:#333
    style M1_BLOCK fill:#c6dbef,stroke:#a4c2f4,color:#333
```

### Backward Planning Logic

```
M6 (Full IQ / Full Speed)
  └─ Prerequisite: M5 all modules integrated + Performance bottlenecks identified
       └─ Prerequisite: M4 extended features integrated (evaluation/favorites/announcements/comments)
            └─ Prerequisite: M3 payment loop established (recharge → balance → order → notification)
                 └─ Prerequisite: M2 products browsable, shopping cart available, orders creatable
                      └─ Prerequisite: M1 users can register, providers can onboard
```

**Core Principle**: Each increment can only proceed to the next after all verification of its prerequisite increments passes.

---

## IV. Figure 3: Dual-Stream Incremental Breakdown

> **Stream A — Business Stream**: Decomposed by core business chain functional stages
> **Stream B — Technology Stream**: Decomposed by system capability building stages

```mermaid
flowchart LR
    subgraph STREAM_A["🔵 Stream A: Business Stream"]
        direction TB
        A1["A1: User System\nlogin + sysuser + company"]
        A2["A2: Product System\nproduct + producttype"]
        A3["A3: Transaction Loop\nshopcart + order + tmoney"]
        A4["A4: Interaction Ecosystem\norderEvalute + comment\n+ userlike + message"]
        A5["A5: Operations Support\nwebnotice + provider\n+ address/distance filter"]

        A1 --> A2 --> A3 --> A4 --> A5
    end

    subgraph STREAM_B["🟢 Stream B: Technology Stream"]
        direction TB
        B1["B1: Foundation Framework\nSpring Boot + MyBatis\n+ MySQL + Thymeleaf"]
        B2["B2: Feature Enhancement\nRedis Cache + File Upload\n+ Pagination + Encryption"]
        B3["B3: Automation\nUnit Tests (JUnit/Mockito)\n+ Integration Tests + Selenium"]
        B4["B4: Performance Optimization\nConnection Pool Tuning + SQL Optimization\n+ Static Resource Cache + Concurrency Config"]
        B5["B5: Production Ready\nDocker Deployment + Nginx Reverse Proxy\n+ Prometheus Monitoring + CI/CD"]

        B1 --> B2 --> B3 --> B4 --> B5
    end

    A1 -.->|"Supports"| B1
    A2 -.->|"Supports"| B2
    A3 -.->|"Validates"| B3
    A4 -.->|"Drives"| B4
    A5 -.->|"Delivers"| B5

    style STREAM_A fill:#e8f0fe,stroke:#1a73e8,stroke-width:2px
    style STREAM_B fill:#e6f4ea,stroke:#137333,stroke-width:2px
```

### Dual-Stream to Milestone Mapping

```mermaid
flowchart TB
    subgraph MILESTONES["Milestone Integration Checkpoints"]
        direction LR
        MS1["M1\nBasic Exposure"]
        MS2["M2\nFirst Order"]
        MS3["M3\n10% Function"]
        MS4["M4\n20% Function"]
        MS5["M5\n50% Function"]
        MS6["M6\nFull IQ"]
    end

    subgraph BUSINESS["🔵 Business Stream Increments"]
        direction LR
        BA1["A1\nUser System"]
        BA2["A2\nProduct System"]
        BA3["A3\nTransaction Loop"]
        BA4["A4\nInteraction Ecosystem"]
        BA5["A5\nOperations Support"]
    end

    subgraph TECH["🟢 Technology Stream Increments"]
        direction LR
        TB1["B1\nFoundation"]
        TB2["B2\nEnhancement"]
        TB3["B3\nAutomation"]
        TB4["B4\nOptimization"]
        TB5["B5\nProduction"]
    end

    BA1 --> BA2 --> BA3 --> BA4 --> BA5
    TB1 --> TB2 --> TB3 --> TB4 --> TB5

    BA1 & TB1 --> MS1
    BA2 & TB1 --> MS2
    BA3 & TB2 --> MS3
    BA4 & TB3 --> MS4
    BA5 & TB4 --> MS5
    BA5 & TB5 --> MS6

    style MILESTONES fill:#fff3e0,stroke:#e65100,stroke-width:2px
    style BUSINESS fill:#e8f0fe,stroke:#1a73e8,stroke-width:2px
    style TECH fill:#e6f4ea,stroke:#137333,stroke-width:2px
```

---

## V. Detailed Verification Criteria by Increment

### M1: Basic Exposure & Acquisition

| Verification Item | Business Stream (A1) | Technology Stream (B1) |
|-------------------|---------------------|------------------------|
| Integration Content | User registration/login, provider onboarding | Spring Boot + MyBatis + MySQL + Thymeleaf setup complete |
| Verification Criteria | Users can register 3 roles (regular user/admin/provider); providers can complete company info | Database connection normal; pages renderable; MyBatis Mapper can execute CRUD |
| KPP | Function Coverage 15% | Foundation framework ready |

### M2: First Order Manual Prep

| Verification Item | Business Stream (A1+A2) | Technology Stream (B1) |
|-------------------|------------------------|------------------------|
| Integration Content | Product/service browsing, category filtering, shopping cart, manual order creation | Thymeleaf template rendering, static resource loading |
| Verification Criteria | Users can browse services published by providers; can add to cart; can manually submit orders | Pages load normally; frontend frameworks (Bootstrap/LayUI) integrated |
| KPP | Function Coverage 35% | Pages interactive |

### M3: 10% Function · Basic Performance

| Verification Item | Business Stream (A1+A2+A3) | Technology Stream (B1+B2) |
|-------------------|---------------------------|---------------------------|
| Integration Content | Recharge application/approval, balance payment, automatic order notification | Redis cache integration, file upload, pagination plugin |
| Verification Criteria | Complete payment loop: recharge → balance → order → deduction → notify provider | Redis read/write normal; file upload available; pagination queries correct |
| KPP | Function Coverage 50% | Response time ≤ 500ms |

### M4: 20% Function · Automation

| Verification Item | Business Stream (A1~A4) | Technology Stream (B1~B3) |
|-------------------|------------------------|---------------------------|
| Integration Content | Order evaluation, user favorites, website announcements, announcement comments | JUnit unit tests, Mockito, integration tests, Selenium |
| Verification Criteria | Users can evaluate completed orders; can favorite services; can view announcements and comment | Core module test coverage ≥ 60%; Selenium smoke tests pass |
| KPP | Function Coverage 75% | Response time ≤ 300ms |

### M5: 50% Function · Performance Optimization

| Verification Item | Business Stream (A1~A5) | Technology Stream (B1~B4) |
|-------------------|------------------------|---------------------------|
| Integration Content | Provider dashboard, address selection, distance filtering | Connection pool tuning, SQL optimization, Nginx static cache, concurrency config |
| Verification Criteria | Providers can view business data (orders/ratings/revenue); users can filter services by distance | 50 concurrent users without errors; slow queries optimized; static resource cache effective |
| KPP | Function Coverage 85% | Response time ≤ 200ms; Concurrent 50 |

### M6: Full IQ · Full Speed

| Verification Item | Business Stream (A1~A5 All) | Technology Stream (B1~B5 All) |
|-------------------|----------------------------|------------------------------|
| Integration Content | Full module end-to-end integration | Docker deployment, Nginx reverse proxy, Prometheus monitoring, GitHub Actions CI/CD |
| Verification Criteria | All business processes end-to-end pass; no P0/P1 defects | Docker Compose one-click startup; Prometheus metrics collectable; CI/CD pipeline passes |
| KPP | **Function Coverage 100%** | **Response ≤ 200ms; Concurrent ≥ 100; Availability ≥ 99%** |

---

## VI. ASCII Architecture Diagrams (No Mermaid Rendering Required)

### Figure 1: Top-Level Milestone Timeline

```
Time ─────────────────────────────────────────────────────────────────────────▶

M1            M2              M3              M4              M5              M6
Basic         First           10%             20%             50%             Full IQ
Exposure      Order           Function        Function        Function        Full Speed
& Acq         Manual          + Basic         + Auto          + Perf          
              Prep            Perf                            Opt             
│             │               │               │               │               │
▼             ▼               ▼               ▼               ▼               ▼
┌─────┐     ┌─────┐        ┌─────┐        ┌─────┐        ┌─────┐        ┌─────┐
│15%  │     │35%  │        │50%  │        │75%  │        │85%  │        │100% │
│Func │────▶│Func │───────▶│Func │───────▶│Func │───────▶│Func │───────▶│Func │
│     │     │Basic│        │≤500ms│       │≤300ms│       │≤200ms│       │≤200ms│
│     │     │Avail│        │     │        │Auto │        │Conc50│       │Conc100│
└─────┘     └─────┘        └─────┘        └─────┘        └─────┘        └─────┘
 login      +product       +order         +Eval/Fav      +Dashboard     Full Integration
 sysuser    producttype    tmoney         +Ann/Comm      +Distance      Docker
 company    shopcart       message                                       Prometheus
```

### Figure 2: Backward Planning Diagram

```
                    ┌─────────────────────────────┐
                    │  M6: Full IQ · Full Speed   │  ◀── Final Goal
                    │  100% Function · Full Perf  │
                    └──────────────┬──────────────┘
                                   │
                          Requires: All modules ready + Performance achieved
                                   │
                    ┌──────────────▼──────────────┐
                    │  M5: 50% Function · Perf Opt │
                    │  85% Function · ≤200ms · C50 │
                    └──────────────┬──────────────┘
                                   │
                          Requires: Core chain complete + Cache layer
                                   │
                    ┌──────────────▼──────────────┐
                    │  M4: 20% Function · Auto     │
                    │  75% Function · ≤300ms · Auto│
                    └──────────────┬──────────────┘
                                   │
                          Requires: Payment loop + Notification mechanism
                                   │
                    ┌──────────────▼──────────────┐
                    │  M3: 10% Function · Basic    │
                    │  50% Function · ≤500ms       │
                    └──────────────┬──────────────┘
                                   │
                          Requires: Products browsable + Shopping flow
                                   │
                    ┌──────────────▼──────────────┐
                    │  M2: First Order Manual Prep │
                    │  35% Function · Basic Avail  │
                    └──────────────┬──────────────┘
                                   │
                          Requires: User system + Provider system
                                   │
                    ┌──────────────▼──────────────┐
                    │  M1: Basic Exposure & Acq   │
                    │  15% Function · Func Verify │
                    └─────────────────────────────┘
```

### Figure 3: Dual-Stream Incremental Breakdown

```
  🔵 Stream A: Business                  🟢 Stream B: Technology
  (Business Stream)                      (Technology Stream)
  ─────────────────                      ────────────────────

  ┌──────────────┐                       ┌──────────────┐
  │ A1: User     │                       │ B1: Foundation│
  │    System    │◄───── Supports ──────│ Spring Boot  │
  │ login        │                       │ MyBatis      │
  │ sysuser      │                       │ MySQL        │
  │ company      │                       └──────┬───────┘
  └──────┬───────┘                              │
         │                                      │
         ▼                                      ▼
  ┌──────────────┐                       ┌──────────────┐
  │ A2: Product  │                       │ B2: Feature  │
  │    System    │◄───── Supports ──────│ Enhancement  │
  │ product      │                       │ Redis Cache  │
  │ producttype  │                       │ File Upload  │
  └──────┬───────┘                       │ Paging/Crypto│
         │                               └──────┬───────┘
         ▼                                      │
  ┌──────────────┐                       ┌──────────────┐
  │ A3: Transac  │                       │ B3: Automation│
  │    Loop      │◄───── Validates ─────│ JUnit/Mockito│
  │ shopcart     │                       │ Integration  │
  │ order        │                       │ Selenium     │
  │ tmoney       │                       └──────┬───────┘
  │ message      │                              │
  └──────┬───────┘                              ▼
         ▼                               ┌──────────────┐
  ┌──────────────┐                       │ B4: Perf Opt │
  │ A4: Interact │◄───── Drives ────────│ Pool Tuning  │
  │    Ecosystem │                       │ SQL Optimize │
  │ orderEvalute │                       │ Static Cache │
  │ comment      │                       │ Concurrency  │
  │ userlike     │                       └──────┬───────┘
  └──────┬───────┘                              │
         │                                      │
         ▼                                      ▼
  ┌──────────────┐                       ┌──────────────┐
  │ A5: Operatns │◄───── Delivers ──────│ B5: Production│
  │    Support   │                       │ Docker       │
  │ webnotice    │                       │ Nginx        │
  │ provider     │                       │ Prometheus   │
  │ Address/Dist │                       │ CI/CD        │
  └──────────────┘                       └──────────────┘

  ══════════════════════════════════════════════════════════════
  Milestone Mapping:
  M1 = A1 + B1     M2 = A1+A2 + B1    M3 = A1~A3 + B1~B2
  M4 = A1~A4 + B1~B3   M5 = A1~A5 + B1~B4   M6 = A1~A5 + B1~B5
```

---

## VII. Mapping to Original Framework (Version 1)

| Original Framework Concept | Version 1 Mapping |
|---------------------------|-------------------|
| Imaging stream | **Business Stream**: Decomposed by core business chain |
| Control & performance stream | **Technology Stream**: Decomposed by system capability building |
| IQ (Image Quality) | **Business Function Coverage**: Integrated modules / 14 total modules |
| Speed | **System Performance Metrics**: Response time, concurrency, availability |
| functioning exposure and acquisition | **M1: Basic Exposure & Acquisition** (registration/login/onboarding) |
| First image manual preparation | **M2: First Order Manual Prep** (browsing/shopping cart/manual order) |
| 10% IQ manual preparation (10% speed) | **M3: 10% Function · Basic Performance** (payment loop/notification/≤500ms) |
| 20% IQ automated preparation (10% speed) | **M4: 20% Function · Automation** (evaluation/favorites/announcements/automated testing) |
| 50% IQ automated preparation (100% speed) | **M5: 50% Function · Performance Optimization** (dashboard/distance filter/concurrent 50) |
| Full IQ Full speed | **M6: Full IQ · Full Speed** (100% function/full performance/Docker/monitoring) |

---

# Version 2: Incremental Integration for 4 New Feature Modules (Incremental Development Scenario)

> ⚠️ **Important Note**: This version applies to **incremental development on existing system**
> - Project has **14 existing business modules** already implemented
> - This incremental development adds **4 new feature modules**
> - Therefore KPP metrics should **only target these 4 new modules**, not the entire system
>
> Comparison with Version 1:
> - Version 1 (above): For greenfield full system development
> - Version 2 (below): For adding features to existing system

---

## I. New Module Description

| No. | Feature Module | English Name | Status | Description |
|-----|---------------|--------------|--------|-------------|
| 1 | User Favorites | Userlike | ✅ Implemented | Users can favorite services of interest |
| 2 | Order Evaluation | OrderEvalute | ✅ Implemented | Users can evaluate service providers after order completion |
| 3 | Customer-Provider Communication | Message | ✅ Implemented | Internal messaging system supporting user-provider communication |
| 4 | AI Pet Knowledge Q&A | AI Pet QA | ❌ Pending | AI-based pet knowledge question answering feature |

---

## II. KPP (Key Performance Parameters) Redefinition

### Problem with Version 1
Version 1's KPP counted "coverage across all 14 system modules", which is correct for **greenfield full system development**. But for incremental development only, using 14 modules as denominator masks the true incremental value.

### Correct KPP Definition (Version 2) — Incorporating Uncertainty

Teacher's requirement: KPP should reflect **uncertainties encountered from current state to target completion**.

Therefore, KPP needs to include **uncertainty dimensions**, reflecting risk levels and verification status at each increment stage.

#### Uncertainty Source Analysis

| Uncertainty Type | Description | Manifestation in This Project |
|-----------------|-------------|------------------------------|
| **Requirements Uncertainty** | Whether requirements are clear, whether they will change | Whether AI QA knowledge scope boundaries are clear |
| **Technical Uncertainty** | Whether technical solution is feasible, whether there are unknown difficulties | AI interface stability, whether response time meets standards |
| **Integration Uncertainty** | Compatibility with existing systems, interface conflict risks | Data consistency between new modules and existing 14 modules |
| **Schedule Uncertainty** | Workload estimation accuracy, dependency delay risks | Whether AI module development cycle is controllable |

#### Improved KPP Definition

| KPP Dimension | KPP Metric | Meaning | Uncertainty Manifestation |
|---------------|------------|---------|--------------------------|
| **Function Completion** | Implemented functions / Planned functions | Technical implementation progress | Uncertainty exists from "implemented" to "verified" |
| **Requirements Certainty** | Confirmed requirements / Total requirements | Requirements stability | Early requirements may change, later gradually converge |
| **Technical Risk Level** | High/Medium/Low risk | Implementation difficulty assessment | AI module has highest risk, favorites/evaluation have lower risk |
| **Integration Verification Status** | Unverified/Partially verified/Fully verified | Compatibility with existing system | Each increment needs regression testing, uncertainty exists |
| **Performance Achievement Rate** | Measured performance / Target performance | System performance maintenance | New features may introduce performance degradation risk |

---

## III. Figure 1 (Version 2): New Module Milestone Timeline

> Left to right, progressing over time. Each milestone indicates new feature completion and performance status.

```mermaid
gantt
    title Pet Service Platform — New Module Incremental Integration Milestones
    dateFormat  YYYY-MM-DD
    axisFormat  %m/%d

    section M1: Favorites Module
    User favorites feature integration      :m1a, 2025-10-01, 7d
    Favorites list page & interaction       :m1b, after m1a, 5d
    Milestone: Favorites Module Complete    :milestone, m1, after m1b, 0d

    section M2: Evaluation Module
    Order evaluation feature integration    :m2a, after m1, 7d
    Provider rating statistics              :m2b, after m2a, 5d
    Milestone: Evaluation Module Complete   :milestone, m2, after m2b, 0d

    section M3: Message Module
    Internal message send/receive           :m3a, after m2, 7d
    User-provider message interconnection   :m3b, after m3a, 5d
    Milestone: Message Module Complete      :milestone, m3, after m3b, 0d

    section M4: AI Pet QA
    AI QA API integration                   :m4a, after m3, 10d
    Pet knowledge base construction         :m4b, after m4a, 7d
    Frontend QA interface integration       :m4c, after m4b, 5d
    Milestone: AI QA Module Complete        :milestone, m4, after m4c, 0d

    section M5: Full Integration Verification
    End-to-end regression testing           :m5a, after m4, 7d
    Performance regression testing          :m5b, after m5a, 5d
    Milestone: Incremental Delivery         :milestone, m5, after m5b, 0d
```

### New Module Milestone KPP Achievement Table (With Uncertainty Dimensions)

| Milestone | Function Completion | Requirements Certainty | Technical Risk | Integration Verification | Performance Achievement | Uncertainty Description |
|-----------|--------------------|------------------------|----------------|-------------------------|------------------------|------------------------|
| **M0: Baseline State** | 0% (0/4) | 100% | None | Verified | 100% | Existing system is stable, lowest uncertainty |
| **M1: Favorites Module Complete** | 25% (1/4) | 100% | 🟢 Low | Verified | 100% | Simple function, clear coupling with existing product module, low uncertainty |
| **M2: Evaluation Module Complete** | 50% (2/4) | 100% | 🟢 Low | Verified | 100% | Rating algorithm determined, but need to verify rating statistics accuracy |
| **M3: Message Module Complete** | 75% (3/4) | 95% | 🟡 Medium | Verified | 100% | Real-time push mechanism has technical uncertainty; message storage growth risk |
| **M4: AI QA Module Complete** | 100% (4/4) | 70% | 🔴 High | Partially Verified | Pending | **Highest uncertainty**: AI interface stability unknown; knowledge base boundaries unclear; response time may not meet standards |
| **M5: Incremental Delivery** | **100%** | **100%** | **None** | **Fully Verified** | **≥95%** | All uncertainties have converged through testing, system reaches deliverable state |

#### Uncertainty Trend Visualization

```
Uncertainty Level
    │
High│                    ┌───┐
    │                    │M4 │ AI Module: Requirements uncertain (70%), High technical risk
    │                ┌───┤   │
 Med│            ┌───┤M3 │   │ Message Module: Real-time push technical uncertainty
    │        ┌───┤   └───┤   │
 Low│    ┌───┤M2 │       └───┤
    │┌───┤   └───┤           │
None│M0 │M1 │   │           │
    └───┴───┴───┴───────────┘
    Baseline Fav Eval Msg    AI    Delivery
                    Function Completion ──────▶
```

**Key Insights**:
- **M0→M3**: Uncertainty is relatively low because functions are standard and technical solutions are mature
- **M4**: Uncertainty **spikes sharply** because AI module involves external dependencies (AI interface) and fuzzy requirements (knowledge boundaries)
- **M5**: Uncertainty **converges to acceptable level** through sufficient testing

---

## IV. Figure 2 (Version 2): Demand-Driven Backward Planning

> Working backward from the final goal (M4: all 4 new modules online), decomposing into integration increments.

```mermaid
flowchart TB
    subgraph TARGET["🎯 Final Goal: M4 — 100% New Features Online + AI QA Complete"]
        M4_V2["M4: All 4 New Modules Integrated\nNew Feature Completion 100%\nAI Pet Knowledge QA Available"]
    end

    subgraph M3_BLOCK["M3: Message Module Complete"]
        M3_V2["M3: Internal Message Interconnection\nNew Feature Completion 75%\nUser-Provider Communication Available"]
    end

    subgraph M2_BLOCK["M2: Evaluation Module Complete"]
        M2_V2["M2: Order Evaluation System\nNew Feature Completion 50%\nUsers Can Evaluate Completed Orders"]
    end

    subgraph M1_BLOCK["M1: Favorites Module Complete"]
        M1_V2["M1: User Favorites Feature\nNew Feature Completion 25%\nUsers Can Favorite/Unfavorite"]
    end

    subgraph M0_BLOCK["M0: Baseline State"]
        M0_V2["M0: Existing 14 Modules\nNew Feature Completion 0%\nBaseline Performance Verified"]
    end

    M4_V2 -->|"Requires: AI QA integration + Knowledge base"| M3_V2
    M3_V2 -->|"Requires: Message read/write + Notification"| M2_V2
    M2_V2 -->|"Requires: Order status association + Rating"| M1_V2
    M1_V2 -->|"Requires: Product association + User binding"| M0_V2

    style TARGET fill:#ff6b35,stroke:#d84315,color:#fff,stroke-width:3px
    style M3_BLOCK fill:#ff8a65,stroke:#ff6b35,color:#fff
    style M2_BLOCK fill:#ffa726,stroke:#ff8a65,color:#fff
    style M1_BLOCK fill:#ffcc80,stroke:#ffa726,color:#333
    style M0_BLOCK fill:#fff3e0,stroke:#ffcc80,color:#333
```

### Backward Planning Logic (Version 2)

```
M4 (New Features 100% + AI QA Complete)
  └─ Prerequisite: M3 message module complete + communication channel available
       └─ Prerequisite: M2 evaluation module complete + rating mechanism ready
            └─ Prerequisite: M1 favorites module complete + user preferences recordable
                 └─ Prerequisite: M0 existing 14 modules baseline verification passed
```

---

## V. Figure 3 (Version 2): Dual-Stream Incremental Breakdown

> **Stream A — Business Stream**: Decomposed by new feature business value chain
> **Stream B — Technology Stream**: Decomposed by new feature implementation and verification stages

```mermaid
flowchart LR
    subgraph STREAM_A_V2["🔵 Stream A: Business Stream (New Features)"]
        direction TB
        AV1["A1: Favorites Module\nuserlike\nUser Preference Recording"]
        AV2["A2: Evaluation Module\norderEvalute\nPost-Transaction Feedback"]
        AV3["A3: Message Module\nmessage\nBidirectional Communication Channel"]
        AV4["A4: AI QA Module\nAI Pet QA\nIntelligent Knowledge Service"]

        AV1 --> AV2 --> AV3 --> AV4
    end

    subgraph STREAM_B_V2["🟢 Stream B: Technology Stream (Implementation & Verification)"]
        direction TB
        BV1["B1: Feature Implementation\nModule Development + API Definition"]
        BV2["B2: Automated Testing\nUnit Tests + Integration Tests\nSelenium UI Tests"]
        BV3["B3: Performance Verification\nResponse Time Testing\nConcurrent Stress Testing"]
        BV4["B4: Regression Verification\nExisting Function Regression Tests\nEnd-to-End Process Verification"]

        BV1 --> BV2 --> BV3 --> BV4
    end

    AV1 -.->|"Technical Support"| BV1
    AV2 -.->|"Test Verification"| BV2
    AV3 -.->|"Function Verification"| BV3
    AV4 -.->|"Delivery Verification"| BV4

    style STREAM_A_V2 fill:#fff3e0,stroke:#ff6b35,stroke-width:2px
    style STREAM_B_V2 fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px
```

### Dual-Stream to Milestone Mapping (Version 2)

```mermaid
flowchart TB
    subgraph MILESTONES_V2["🎯 Milestone Integration Checkpoints"]
        direction LR
        MV1["M1\nFavorites Complete"]
        MV2["M2\nEvaluation Complete"]
        MV3["M3\nMessage Complete"]
        MV4["M4\nAI QA Complete"]
        MV5["M5\nIncremental Delivery"]
    end

    subgraph BUSINESS_V2["🔵 Business Stream Increments"]
        direction LR
        BAV1["A1\nFavorites"]
        BAV2["A2\nEvaluation"]
        BAV3["A3\nMessage"]
        BAV4["A4\nAI QA"]
    end

    subgraph TECH_V2["🟢 Technology Stream Increments"]
        direction LR
        TBV1["B1\nFeature Implementation"]
        TBV2["B2\nAutomated Testing"]
        TBV3["B3\nPerformance Verification"]
        TBV4["B4\nRegression Verification"]
    end

    BAV1 --> BAV2 --> BAV3 --> BAV4
    TBV1 --> TBV2 --> TBV3 --> TBV4

    BAV1 & TBV1 --> MV1
    BAV2 & TBV2 --> MV2
    BAV3 & TBV2 --> MV3
    BAV4 & TBV3 --> MV4
    BAV4 & TBV4 --> MV5

    style MILESTONES_V2 fill:#fff3e0,stroke:#ff6b35,stroke-width:2px
    style BUSINESS_V2 fill:#fff3e0,stroke:#ff6b35,stroke-width:2px
    style TECH_V2 fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px
```

---

## VI. Detailed Verification Criteria by Increment (Version 2)

### M0: Baseline State

| Verification Item | Description |
|-------------------|-------------|
| Integration Content | Existing 14 business modules (login, sysuser, company, product, producttype, order, shopcart, tmoney, provider, webnotice, comment, type, common, web) |
| Verification Criteria | Core business processes (registration → browsing → ordering → payment → viewing orders) end-to-end pass |
| KPP | New Feature Completion 0%; Existing Function Verification Rate 100% |

### M1: Favorites Module Complete

| Verification Item | Business Stream (A1) | Technology Stream (B1) |
|-------------------|---------------------|------------------------|
| Integration Content | Users can favorite/unfavorite products; favorites list display | Favorites module unit tests pass; integration tests pass |
| Verification Criteria | User clicks favorite and data correctly stored; unfavorite updates list; no conflicts with product module | Favorites related APIs respond normally; existing functions have no regression |
| KPP | New Feature Completion 25% (1/4) | Existing performance maintained |

### M2: Evaluation Module Complete

| Verification Item | Business Stream (A2) | Technology Stream (B1+B2) |
|-------------------|---------------------|---------------------------|
| Integration Content | Users can evaluate after order completion; provider ratings auto-update | Evaluation module automated test coverage; Selenium UI tests pass |
| Verification Criteria | Completed orders show evaluation entry; rating statistics update after submission; evaluation list displays correctly | Evaluation related APIs respond normally; rating calculation logic correct; existing functions have no regression |
| KPP | New Feature Completion 50% (2/4) | Existing performance maintained |

### M3: Message Module Complete

| Verification Item | Business Stream (A3) | Technology Stream (B2) |
|-------------------|---------------------|------------------------|
| Integration Content | Users can send messages to providers; providers can reply; message notifications | Message module automated test coverage; real-time message push verification |
| Verification Criteria | User-provider bidirectional message interconnection; unread message marking; message list pagination correct | Message APIs respond normally; push mechanism works normally; existing functions have no regression |
| KPP | New Feature Completion 75% (3/4) | Existing performance maintained |

### M4: AI QA Module Complete — Highest Uncertainty Increment

| Verification Item | Business Stream (A4) | Technology Stream (B2+B3) |
|-------------------|---------------------|---------------------------|
| Integration Content | AI pet knowledge Q&A; pet knowledge base; QA interface | AI QA API tests; performance tests |
| Verification Criteria | User asks question and AI returns relevant answer; answer content matches pet knowledge theme; QA response time ≤ 3 seconds | AI API availability 99%; response time ≤ 3 seconds; existing functions have no regression |
| KPP | New Feature Completion 100% (4/4) | New feature performance achieved |

#### M4 Uncertainty Detailed Analysis

| Uncertainty Type | Risk Description | Mitigation Measure | Verification Method |
|-----------------|------------------|-------------------|---------------------|
| **Requirements Uncertainty** | AI knowledge boundaries unclear: which pet questions can be answered? How to handle medical advice? | Define knowledge scope checklist; set disclaimer | Requirements review meeting; boundary test cases |
| **Technical Uncertainty** | AI interface response time unstable; API rate limiting/failure | Implement local cache; fallback strategy (return default prompt) | Stress testing; fault injection testing |
| **Integration Uncertainty** | AI module coupling with existing user system; data format incompatibility | Define clear API contract; data transformation layer | Interface contract testing; integration testing |
| **Schedule Uncertainty** | AI knowledge base construction workload hard to estimate; tuning cycle long | Phased delivery: general knowledge first, professional knowledge later | Iterative development; weekly progress review |

**Risk Response Contingency Plans**:
- If AI interface is unstable → Switch to backup AI provider or downgrade to FAQ mode
- If response time does not meet standard → Introduce preloading and caching mechanism
- If knowledge base construction lags → Prioritize covering high-frequency questions, supplement others gradually

### M5: Incremental Delivery

| Verification Item | Business Stream (A1~A4 All) | Technology Stream (B1~B4 All) |
|-------------------|----------------------------|------------------------------|
| Integration Content | 4 new modules end-to-end integration | Complete test coverage + performance verification + regression tests |
| Verification Criteria | All new feature business processes end-to-end pass; no P0/P1 defects | Automated test coverage ≥ 80%; performance tests pass; regression tests pass |
| KPP | **New Feature Completion 100%** | **Existing performance maintained + new feature performance achieved** |

---

## VII. Version Comparison Table

| Comparison Dimension | Version 1 (Full System Integration) | Version 2 (Incremental Development) |
|---------------------|-------------------------------------|-------------------------------------|
| **Applicable Scenario** | Building entire system from scratch | Adding features to existing system |
| **Denominator Baseline** | 14 full system business modules | 4 new feature modules this time |
| **KPP Function Dimension** | Full system business function coverage | New feature completion rate |
| **Number of Milestones** | 6 (M1~M6) | 5 (M0~M4) + M5 delivery |
| **Technology Stream Focus** | Complete construction from foundation to production | New module test coverage and performance verification |
| **Business Stream Focus** | Complete chain from user system to operations support | Value increment from favorites to AI QA |

### Core Difference Illustration

```
Version 1 (Full System Integration):
┌─────────────────────────────────────────────────────────────┐
│  KPP = Integrated Modules / Total Modules (14)              │
│                                                             │
│  0% ────────────── 15% ────── 50% ────── 75% ─── 100%     │
│  (None)  M1 Basic  M2 First  M3 Payment M4 Extended M6 Full│
└─────────────────────────────────────────────────────────────┘

Version 2 (Incremental Development):
┌─────────────────────────────────────────────────────────────┐
│  KPP = Completed New Modules / Total New Modules (4)        │
│                                                             │
│  0% ───── 25% ────── 50% ────── 75% ────── 100%           │
│  (Baseline) M1 Fav  M2 Eval   M3 Msg   M4 AI              │
│                                                             │
│  Existing 14 Modules ───────────────────────────────────▶   │
│  (Already 100% complete, as baseline, not in KPP denominator)│
└─────────────────────────────────────────────────────────────┘
```

---

## VIII. Mapping to Original Framework (Version 2)

| Original Framework Concept | Version 2 Mapping |
|---------------------------|-------------------|
| Imaging stream | **Business Stream (New Features)**: Decomposed by new feature business value chain |
| Control & performance stream | **Technology Stream (Implementation & Verification)**: Decomposed by new feature technical implementation stages |
| IQ (Image Quality) | **New Feature Completion Rate**: Integrated new modules / 4 new modules |
| Speed | **System Performance Impact**: Impact of new features on existing system performance |
| functioning exposure and acquisition | **M1: Favorites Module Complete** (user preference recording feature) |
| First image manual preparation | **M2: Evaluation Module Complete** (post-transaction feedback mechanism) |
| 10% IQ manual preparation (10% speed) | **M3: Message Module Complete** (bidirectional communication channel establishment) |
| 20% IQ automated preparation (10% speed) | **M4: AI QA Module Implementation** (intelligent knowledge service online) |
| 50% IQ automated preparation (100% speed) | **M5: Performance Verification** (performance tests passed) |
| Full IQ Full speed | **Incremental Delivery**: 4 new modules 100% complete + performance achieved + regression tests passed |
