const { Document, Packer, Paragraph, TextRun, Table, TableRow, TableCell, 
        HeadingLevel, AlignmentType, BorderStyle, WidthType, ShadingType, LevelFormat } = require('docx');
const fs = require('fs');

const border = { style: BorderStyle.SINGLE, size: 1, color: "999999" };
const borders = { top: border, bottom: border, left: border, right: border };

const doc = new Document({
  styles: {
    default: {
      document: {
        run: {
          font: { ascii: "Arial", hAnsi: "Arial", eastAsia: "Microsoft YaHei" },
          size: 22
        }
      }
    },
    paragraphStyles: [
      { id: "Heading1", name: "Heading 1", basedOn: "Normal", next: "Normal", quickFormat: true,
        run: { size: 28, bold: true, font: { ascii: "Arial", hAnsi: "Arial", eastAsia: "Microsoft YaHei" } },
        paragraph: { spacing: { before: 200, after: 200 }, outlineLevel: 0, keepNext: false, keepLines: false } },
      { id: "Heading2", name: "Heading 2", basedOn: "Normal", next: "Normal", quickFormat: true,
        run: { size: 24, bold: true, font: { ascii: "Arial", hAnsi: "Arial", eastAsia: "Microsoft YaHei" } },
        paragraph: { spacing: { before: 160, after: 160 }, outlineLevel: 1, keepNext: false, keepLines: false } },
    ]
  },
  numbering: {
    config: [
      { reference: "bullets",
        levels: [{ level: 0, format: LevelFormat.BULLET, text: "•", alignment: AlignmentType.LEFT,
          style: { paragraph: { indent: { left: 720, hanging: 360 } } } }] },
    ]
  },
  sections: [{
    properties: {
      page: {
        size: { width: 12240, height: 15840 },
        margin: { top: 1440, right: 1440, bottom: 1440, left: 1440 }
      }
    },
    children: [
      // Title
      new Paragraph({
        heading: HeadingLevel.HEADING_1,
        alignment: AlignmentType.CENTER,
        children: [new TextRun("Day 4 Assignment: Early Integration Strategy")]
      }),
      new Paragraph({
        alignment: AlignmentType.CENTER,
        spacing: { after: 300 },
        children: [new TextRun("Pet Service Platform — AI Knowledge Q&A Module")]
      }),

      // 1. Hot KPP Selection
      new Paragraph({
        heading: HeadingLevel.HEADING_2,
        children: [new TextRun("1. Hot KPP Selection")]
      }),
      new Paragraph({
        spacing: { after: 120 },
        children: [new TextRun("Based on the incremental integration analysis, the "),
          new TextRun({ text: "Requirements Certainty of AI Pet Knowledge Q&A Module", bold: true }),
          new TextRun(" is selected as the hot KPP.")]
      }),
      new Paragraph({
        spacing: { after: 120 },
        children: [new TextRun({ text: "Rationale: ", bold: true }),
          new TextRun("Among all KPPs tracked across the integration sequence (M0-M5), this metric shows the highest uncertainty spike—from 95% certainty at M3 (Message Module) to only 70% at M4 (AI Module). This 25% drop indicates significant unknowns that threaten project success if not addressed early.")]
      }),

      // KPP Table
      new Table({
        width: { size: 100, type: WidthType.PERCENTAGE },
        columnWidths: [2500, 3500, 3500],
        rows: [
          new TableRow({
            cantSplit: true,
            children: [
              new TableCell({
                borders,
                shading: { fill: "4472C4", type: ShadingType.CLEAR },
                width: { size: 2500, type: WidthType.DXA },
                children: [new Paragraph({ children: [new TextRun({ text: "KPP Dimension", bold: true, color: "FFFFFF" })] })]
              }),
              new TableCell({
                borders,
                shading: { fill: "4472C4", type: ShadingType.CLEAR },
                width: { size: 3500, type: WidthType.DXA },
                children: [new Paragraph({ children: [new TextRun({ text: "M3 (Message Module)", bold: true, color: "FFFFFF" })] })]
              }),
              new TableCell({
                borders,
                shading: { fill: "4472C4", type: ShadingType.CLEAR },
                width: { size: 3500, type: WidthType.DXA },
                children: [new Paragraph({ children: [new TextRun({ text: "M4 (AI QA Module)", bold: true, color: "FFFFFF" })] })]
              })
            ]
          }),
          new TableRow({
            cantSplit: true,
            children: [
              new TableCell({ borders, width: { size: 2500, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("Requirements Certainty")] })] }),
              new TableCell({ borders, width: { size: 3500, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("95% — Clear scope")] })] }),
              new TableCell({ borders, width: { size: 3500, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun({ text: "70% — Fuzzy boundaries", color: "C00000" })] })] })
            ]
          }),
          new TableRow({
            cantSplit: true,
            children: [
              new TableCell({ borders, width: { size: 2500, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("Technical Risk")] })] }),
              new TableCell({ borders, width: { size: 3500, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("Medium — Known tech")] })] }),
              new TableCell({ borders, width: { size: 3500, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun({ text: "High — External dependency", color: "C00000" })] })] })
            ]
          }),
          new TableRow({
            cantSplit: true,
            children: [
              new TableCell({ borders, width: { size: 2500, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("Integration Status")] })] }),
              new TableCell({ borders, width: { size: 3500, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("Verified")] })] }),
              new TableCell({ borders, width: { size: 3500, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun({ text: "Partially Verified", color: "C00000" })] })] })
            ]
          })
        ]
      }),
      new Paragraph({ spacing: { after: 200 }, children: [] }),

      // 2. Two Critical Assumptions
      new Paragraph({
        heading: HeadingLevel.HEADING_2,
        children: [new TextRun("2. Two Critical Assumptions That Could Cause KPP Failure")]
      }),

      new Paragraph({
        spacing: { before: 120, after: 80 },
        children: [new TextRun({ text: "Assumption 1: AI Interface Response Time", bold: true })]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun({ text: "Statement: ", bold: true }), new TextRun("The AI Q&A API can respond within 3 seconds for 95% of requests.")]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun({ text: "Failure scenario: ", bold: true }), new TextRun("If actual response time is 5-10 seconds, user experience becomes unacceptable, leading to feature abandonment.")]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun({ text: "Root cause: ", bold: true }), new TextRun("External AI service latency, network instability, or complex query processing time underestimated.")]
      }),

      new Paragraph({
        spacing: { before: 160, after: 80 },
        children: [new TextRun({ text: "Assumption 2: AI Answer Accuracy", bold: true })]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun({ text: "Statement: ", bold: true }), new TextRun("The AI can provide accurate and relevant answers to pet-related questions with ≥80% accuracy.")]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun({ text: "Failure scenario: ", bold: true }), new TextRun("If accuracy drops below 70%, users lose trust, and the feature becomes a liability rather than an asset.")]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun({ text: "Root cause: ", bold: true }), new TextRun("Knowledge base scope unclear; AI model not fine-tuned for pet domain; ambiguous user questions.")]
      }),
      new Paragraph({ spacing: { after: 200 }, children: [] }),

      // 3. Early Integration Increment
      new Paragraph({
        heading: HeadingLevel.HEADING_2,
        children: [new TextRun("3. Proposed Early Integration Increment: M3.5 AI Prototype")]
      }),
      new Paragraph({
        spacing: { after: 120 },
        children: [new TextRun("Rather than waiting until M4 to integrate the full AI module, we insert an "),
          new TextRun({ text: "early integration increment (M3.5)", bold: true }),
          new TextRun(" between M3 and M4 specifically designed to expose risks.")]
      }),

      new Paragraph({
        spacing: { before: 120, after: 80 },
        children: [new TextRun({ text: "M3.5 Scope (Minimum Viable Integration):", bold: true })]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("Backend: AI API integration with basic request/response handling")]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("Test harness: 50 predefined pet-related questions covering common scenarios")]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("No full knowledge base or frontend UI—focus purely on API validation")]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("Duration: 3-5 days (short, time-boxed)")]
      }),

      new Paragraph({
        spacing: { before: 160, after: 80 },
        children: [new TextRun({ text: "Why this exposes risk early:", bold: true })]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("Tests the highest-risk component (AI API) before committing to full development")]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("Provides concrete data on response time and accuracy within one week")]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("If assumptions fail, we can pivot early—switch AI providers, adjust scope, or implement fallback strategies")]
      }),
      new Paragraph({ spacing: { after: 200 }, children: [] }),

      // 4. Validation Activity
      new Paragraph({
        heading: HeadingLevel.HEADING_2,
        children: [new TextRun("4. Validation Activity: Comprehensive AI API Stress Testing")]
      }),

      new Table({
        width: { size: 100, type: WidthType.PERCENTAGE },
        columnWidths: [2200, 4000, 3300],
        rows: [
          new TableRow({
            cantSplit: true,
            children: [
              new TableCell({
                borders,
                shading: { fill: "70AD47", type: ShadingType.CLEAR },
                width: { size: 2200, type: WidthType.DXA },
                children: [new Paragraph({ children: [new TextRun({ text: "Test Category", bold: true, color: "FFFFFF" })] })]
              }),
              new TableCell({
                borders,
                shading: { fill: "70AD47", type: ShadingType.CLEAR },
                width: { size: 4000, type: WidthType.DXA },
                children: [new Paragraph({ children: [new TextRun({ text: "Methodology", bold: true, color: "FFFFFF" })] })]
              }),
              new TableCell({
                borders,
                shading: { fill: "70AD47", type: ShadingType.CLEAR },
                width: { size: 3300, type: WidthType.DXA },
                children: [new Paragraph({ children: [new TextRun({ text: "Pass Criteria", bold: true, color: "FFFFFF" })] })]
              })
            ]
          }),
          new TableRow({
            cantSplit: true,
            children: [
              new TableCell({ borders, width: { size: 2200, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("Response Time Test")] })] }),
              new TableCell({ borders, width: { size: 4000, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("Send 500+ requests with varied question complexity; measure P50, P95, P99 response times")] })] }),
              new TableCell({ borders, width: { size: 3300, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("P95 ≤ 3 seconds; P99 ≤ 5 seconds")] })] })
            ]
          }),
          new TableRow({
            cantSplit: true,
            children: [
              new TableCell({ borders, width: { size: 2200, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("Accuracy Assessment")] })] }),
              new TableCell({ borders, width: { size: 4000, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("50 predefined questions with expert-verified answers; blind evaluation by 3 reviewers")] })] }),
              new TableCell({ borders, width: { size: 3300, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("≥80% rated as accurate and helpful")] })] })
            ]
          }),
          new TableRow({
            cantSplit: true,
            children: [
              new TableCell({ borders, width: { size: 2200, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("Fault Injection")] })] }),
              new TableCell({ borders, width: { size: 4000, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("Simulate API timeout (10s), rate limiting (429 error), and service unavailable (503)")] })] }),
              new TableCell({ borders, width: { size: 3300, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("System degrades gracefully; user sees helpful error message")] })] })
            ]
          }),
          new TableRow({
            cantSplit: true,
            children: [
              new TableCell({ borders, width: { size: 2200, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("Load Test")] })] }),
              new TableCell({ borders, width: { size: 4000, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("100 concurrent users sending requests for 10 minutes")] })] }),
              new TableCell({ borders, width: { size: 3300, type: WidthType.DXA }, children: [new Paragraph({ children: [new TextRun("No degradation in response time; error rate < 1%")] })] })
            ]
          })
        ]
      }),
      new Paragraph({ spacing: { after: 200 }, children: [] }),

      // 5. Expected Learning
      new Paragraph({
        heading: HeadingLevel.HEADING_2,
        children: [new TextRun("5. Expected Learning from This Step")]
      }),

      new Paragraph({
        spacing: { before: 120, after: 80 },
        children: [new TextRun({ text: "Technical Feasibility:", bold: true })]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("Can the AI API meet performance requirements? If not, we need to explore caching, pre-computation, or alternative providers.")]
      }),

      new Paragraph({
        spacing: { before: 120, after: 80 },
        children: [new TextRun({ text: "Requirements Refinement:", bold: true })]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("What types of questions does the AI handle well? Where does it fail? This informs knowledge base scope and user guidance.")]
      }),

      new Paragraph({
        spacing: { before: 120, after: 80 },
        children: [new TextRun({ text: "Integration Risk Assessment:", bold: true })]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("Are there hidden coupling issues between AI module and existing user system? Early detection prevents late-stage refactoring.")]
      }),

      new Paragraph({
        spacing: { before: 120, after: 80 },
        children: [new TextRun({ text: "Decision Point:", bold: true })]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun({ text: "Go/No-Go: ", bold: true }), new TextRun("If both assumptions pass, proceed to full M4 development. If either fails, implement contingency plans before committing resources.")]
      }),

      new Paragraph({
        spacing: { before: 200, after: 80 },
        children: [new TextRun({ text: "Contingency Plans (if assumptions fail):", bold: true, italics: true })]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("Response time > 3s: Implement local caching + fallback to FAQ database")]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("Accuracy < 80%: Narrow knowledge scope to high-confidence topics; add human review loop")]
      }),
      new Paragraph({
        numbering: { reference: "bullets", level: 0 },
        children: [new TextRun("API unreliable: Switch to backup AI provider or hybrid approach (AI + rule-based)")]
      })
    ]
  }]
});

Packer.toBuffer(doc).then(buffer => {
  fs.writeFileSync("Day4_Assignment_Early_Integration_Strategy.docx", buffer);
  console.log("Document created successfully!");
});
